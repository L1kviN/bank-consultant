var map;
var placemarks = [];

function calculateDistance(lat1, lon1, lat2, lon2) {
    var R = 6371;
    var dLat = (lat2 - lat1) * Math.PI / 180;
    var dLon = (lon2 - lon1) * Math.PI / 180;
    var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
        Math.sin(dLon/2) * Math.sin(dLon/2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c;
}

function updateOfficesList(curUserLat, curUserLng, officesArray) {
    try {
        var container = document.getElementById('officesList');
        if (!container) return;

        var listData = officesArray || officesData;
        var officesWithDistance = [];

        for (var i = 0; i < listData.length; i++) {
            var office = listData[i];
            if (curUserLat && curUserLng && office.lat && office.lng) {
                var dist = calculateDistance(curUserLat, curUserLng, office.lat, office.lng);
                officesWithDistance.push({office: office, distance: dist});
            } else {
                officesWithDistance.push({office: office, distance: null});
            }
        }

        if (curUserLat && curUserLng) {
            officesWithDistance.sort(function(a, b) {
                return a.distance - b.distance;
            });
        }

        container.innerHTML = '';
        for (var i = 0; i < officesWithDistance.length; i++) {
            var item = officesWithDistance[i];
            var office = item.office;
            var div = document.createElement('div');
            div.className = 'office-item';
            var distanceHtml = '';
            
            if (item.distance !== null) {
                distanceHtml = '<div style="color: #3b82f6; font-size: 12px; margin-top: 4px;">📍 Расстояние: ' + item.distance.toFixed(1) + ' км</div>';
            }
            
            var addrHtml = office.address ? office.address : 'Без адреса';
            var timeHtml = office.workTime ? '<div class="office-detail">🕐 ' + office.workTime + '</div>' : '';
            var phoneHtml = office.phone ? '<div class="office-detail">📞 ' + office.phone + '</div>' : '';
            
            div.innerHTML = '<div class="office-address">🏢 ' + addrHtml + '</div>' + timeHtml + phoneHtml + distanceHtml;
            
            div.onclick = (function(lat, lng) {
                return function() { 
                    if(map && lat && lng) map.setCenter([lat, lng], 15); 
                };
            })(office.lat, office.lng);
            
            container.appendChild(div);
        }
    } catch(e) {
        console.error('Ошибка при обновлении списка', e);
    }
}

function initMap(offices, curUserLat, curUserLng) {
    var centerLat = curUserLat || 55.751244;
    var centerLng = curUserLng || 37.618423;
    var zoom = curUserLat ? 12 : 10;

    if (map) {
        map.destroy();
    }

    map = new ymaps.Map('map', {
        center: [centerLat, centerLng],
        zoom: zoom,
        controls: ['zoomControl', 'fullscreenControl']
    });

    if (placemarks.length) {
        map.geoObjects.remove(placemarks);
        placemarks = [];
    }

    var bounds = [];

    for (var i = 0; i < offices.length; i++) {
        var office = offices[i];
        var lat = office.lat;
        var lng = office.lng;

        if (lat && lng && !isNaN(lat) && !isNaN(lng)) {
            var balloonContent = '<b>' + office.address + '</b>';
            if (office.workTime) balloonContent += '<br>🕐 ' + office.workTime;
            if (office.phone) balloonContent += '<br>📞 ' + office.phone;

            var placemark = new ymaps.Placemark([lat, lng], {
                balloonContent: balloonContent,
                hintContent: office.address
            }, {
                preset: 'islands#blueBankIcon'
            });

            map.geoObjects.add(placemark);
            placemarks.push(placemark);
            bounds.push([lat, lng]);
        }
    }

    if (curUserLat && curUserLng) {
        var userPlacemark = new ymaps.Placemark([curUserLat, curUserLng], {
            balloonContent: '📍 Вы здесь',
            hintContent: 'Ваше местоположение'
        }, {
            preset: 'islands#redCircleIcon'
        });
        map.geoObjects.add(userPlacemark);
        placemarks.push(userPlacemark);
        bounds.push([curUserLat, curUserLng]);
    }

    var mapBounds = map.geoObjects.getBounds();
    if (mapBounds) {
        map.setBounds(mapBounds, {
            checkZoomRange: true,
            zoomMargin: 50
        });
    }
}

ymaps.ready(function() {
    try {
        var maxDistanceKm = 60; // Радиус
        var filteredOffices = [];
        var userCity = localStorage.getItem('userCity');

        for (var i = 0; i < officesData.length; i++) {
            var office = officesData[i];
            var isMatch = false;

            // Фильтр 1: по названию города (если есть в localstorage)
            if (userCity && userCity !== 'неизвестно' && office.address) {
                if (office.address.toLowerCase().includes(userCity.toLowerCase())) {
                    isMatch = true;
                }
            }
            
            // Фильтр 2: по радиусу (если есть геолокация)
            if (!isMatch && userLat && userLng && office.lat && office.lng) {
                var dist = calculateDistance(userLat, userLng, office.lat, office.lng);
                if (dist <= maxDistanceKm) {
                    isMatch = true;
                }
            }

            if (isMatch) {
                filteredOffices.push(office);
            }
        }

        // fallback, если ничего не определено
        if (!userLat && !userLng && (!userCity || userCity === 'неизвестно')) {
            filteredOffices = officesData;
        }

        initMap(filteredOffices, userLat, userLng);
        updateOfficesList(userLat, userLng, filteredOffices);

        var listContainer = document.getElementById('officesList');
        if (filteredOffices.length === 0 && listContainer) {
            listContainer.innerHTML = '<div style="color: #94a3b8; padding: 20px;">Отделения в данном городе не найдены. (Всего отделений в базе банка: ' + officesData.length + ')</div>';
        }
    } catch(e) {
        var listContainerErr = document.getElementById('officesList');
        if (listContainerErr) {
            listContainerErr.innerHTML = '<div style="color: red; padding: 20px;">Ошибка JS: ' + e.message + '</div>';
        }
    }
});
