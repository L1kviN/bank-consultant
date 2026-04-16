var map;
var placemarks = [];

function calculateDistance(lat1, lon1, lat2, lon2) {
    if (!lat1 || !lon1 || !lat2 || !lon2) return null;
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

        var listData = officesArray || [];
        var officesWithDistance = [];

        for (var i = 0; i < listData.length; i++) {
            var office = listData[i];
            var dist = calculateDistance(curUserLat, curUserLng, office.lat, office.lng);
            officesWithDistance.push({office: office, distance: dist});
        }

        if (curUserLat && curUserLng) {
            officesWithDistance.sort(function(a, b) {
                return (a.distance || 9999) - (b.distance || 9999);
            });
        }

        container.innerHTML = '';
        if (officesWithDistance.length === 0) {
            container.innerHTML = '<div style="color: #94a3b8; padding: 20px;">Отделения в радиусе 150км не найдены.</div>';
            return;
        }

        for (var i = 0; i < officesWithDistance.length; i++) {
            var item = officesWithDistance[i];
            var office = item.office;
            var div = document.createElement('div');
            div.className = 'office-item';
            
            var distanceHtml = '';
            if (item.distance !== null) {
                var color = item.distance < 10 ? '#22c55e' : (item.distance < 50 ? '#eab308' : '#ef4444');
                distanceHtml = '<div style="color: ' + color + '; font-size: 13px; margin-top: 6px; font-weight: 500;">📍 Расстояние: ' + item.distance.toFixed(1) + ' км</div>';
            }
            
            var addrHtml = office.address ? office.address : 'Без точного адреса';
            var timeHtml = office.workTime ? '<div class="office-detail">🕐 ' + office.workTime + '</div>' : '';
            var phoneHtml = office.phone ? '<div class="office-detail">📞 ' + office.phone + '</div>' : '';
            
            div.innerHTML = '<div class="office-address">🏢 ' + addrHtml + '</div>' + timeHtml + phoneHtml + distanceHtml;
            
            div.onclick = (function(lat, lng) {
                return function() { 
                    if(map && lat && lng) map.setCenter([lat, lng], 15, {duration: 500}); 
                };
            })(office.lat, office.lng);
            
            container.appendChild(div);
        }
    } catch(e) {
        console.error('Ошибка при обновлении списка', e);
    }
}

function initMap(initialOffices, curUserLat, curUserLng) {
    var centerLat = curUserLat || 55.751244;
    var centerLng = curUserLng || 37.618423;
    var zoom = curUserLat ? 11 : 5;

    if (map) map.destroy();

    // Запускаем карту ТОЛЬКО с нужными контролами (убираем поиск и прочее)
    map = new ymaps.Map('map', {
        center: [centerLat, centerLng],
        zoom: zoom,
        controls: ['zoomControl', 'geolocationControl']
    }, {
        suppressMapOpenBlock: true // Скрывает кнопку "Открыть в Яндекс Картах" если позволяет тариф API
    });

    // Очищаем старые метки
    map.geoObjects.removeAll();

    // Добавляем геолокацию пользователя
    if (curUserLat && curUserLng) {
        var userPlacemark = new ymaps.Placemark([curUserLat, curUserLng], {
            balloonContent: '📍 Ваше текущее местоположение',
            hintContent: 'Вы здесь'
        }, {
            preset: 'islands#redCircleIcon'
        });
        map.geoObjects.add(userPlacemark);
    }

    var bankNameSpan = document.querySelector('.bank-title span');
    var realBankName = bankNameSpan ? bankNameSpan.innerText.trim() : 'Банк';

    // Тихий (безинтерфейсный) поиск через ymaps.search
    ymaps.search(realBankName + ' отделения', {
        results: 15,
        boundedBy: map.getBounds(),
        strictBounds: false
    }).then(function (res) {
        var dynamicOffices = [];
        var geoObjects = res.geoObjects.toArray();

        if (geoObjects && geoObjects.length > 0) {
            geoObjects.forEach(function (obj) {
                var coords = obj.geometry.getCoordinates();
                var props = obj.properties.getAll();
                
                var address = props.name + (props.description ? ' (' + props.description + ')' : '');
                var workTime = props.CompanyMetaData && props.CompanyMetaData.Hours ? props.CompanyMetaData.Hours.text : 'Часы работы не указаны';
                var phone = props.CompanyMetaData && props.CompanyMetaData.Phones && props.CompanyMetaData.Phones.length > 0 ? props.CompanyMetaData.Phones[0].formatted : 'Телефон не указан';
                
                dynamicOffices.push({
                    lat: coords[0],
                    lng: coords[1],
                    address: address,
                    workTime: workTime,
                    phone: phone
                });

                // Создаем и добавляем реальную метку на карту!
                var placemark = new ymaps.Placemark(coords, {
                    balloonContent: '<b>' + address + '</b><br>🕐 ' + workTime + '<br>📞 ' + phone,
                    hintContent: address
                }, { 
                    preset: 'islands#blueBankIcon' 
                });
                map.geoObjects.add(placemark);
            });
        }

        // Добавляем данные из нашей БД, если они тоже близко
        for (var i = 0; i < initialOffices.length; i++) {
            var dbOffice = initialOffices[i];
            if (curUserLat && curUserLng) {
                var dist = calculateDistance(curUserLat, curUserLng, dbOffice.lat, dbOffice.lng);
                // Показываем из БД только те, что ближе 150км
                if (dist !== null && dist < 150) {
                    dynamicOffices.push(dbOffice);
                    
                    var placemark = new ymaps.Placemark([dbOffice.lat, dbOffice.lng], {
                        balloonContent: '<b>' + dbOffice.address + '</b><br>🕐 ' + (dbOffice.workTime||'') + '<br>📞 ' + (dbOffice.phone||''),
                    }, { preset: 'islands#blueBankIcon' });
                    map.geoObjects.add(placemark);
                }
            }
        }

        // Обновляем список отсортированными отделениями
        updateOfficesList(curUserLat, curUserLng, dynamicOffices);
        
        // Подгоняем масштаб карты, чтобы всё поместилось
        var bounds = map.geoObjects.getBounds();
        if (bounds) {
            map.setBounds(bounds, { checkZoomRange: true, zoomMargin: 40 });
        }
    }).catch(function(error) {
        console.error('Ошибка поиска Яндекс', error);
    });
}

ymaps.ready(function() {
    try {
        initMap(officesData || [], userLat, userLng);
    } catch(e) {
        var listContainerErr = document.getElementById('officesList');
        if (listContainerErr) {
            listContainerErr.innerHTML = '<div style="color: red; padding: 20px;">Ошибка загрузки карты: ' + e.message + '</div>';
        }
    }
});
