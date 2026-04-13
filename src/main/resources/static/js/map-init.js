let map;

function initYandexMap() {
    // 1. Берем координаты из сервера (если есть), иначе из localStorage, иначе дефолт Москва
    let lat = window.serverLat || localStorage.getItem('userLat') || 55.75;
    let lng = window.serverLng || localStorage.getItem('userLng') || 37.62;

    ymaps.ready(() => {
        map = new ymaps.Map('map', {
            center: [parseFloat(lat), parseFloat(lng)],
            zoom: 12
        });

        // Отрисовка отделений (officesData передается из HTML)
        if (window.officesData) {
            window.officesData.forEach(office => {
                const p = new ymaps.Placemark([office.lat, office.lng], {
                    balloonContent: `<b>${office.address}</b><br>${office.workTime}`
                }, { preset: 'islands#blueBankIcon' });
                map.geoObjects.add(p);
            });
        }
    });
}

// Запрос геолокации кнопкой
function requestLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(pos => {
            const { latitude, longitude } = pos.coords;
            localStorage.setItem('userLat', latitude);
            localStorage.setItem('userLng', longitude);
            map.setCenter([latitude, longitude], 14);
            document.getElementById('geoStatus').innerText = '✅ Геолокация обновлена';
        });
    }
}

document.addEventListener('DOMContentLoaded', initYandexMap);