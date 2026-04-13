// ==================== КАЛЬКУЛЯТОР ====================
const avgRate = 15.0;

function calculateMonthlyPayment(amount, months, rate) {
    if (amount <= 0 || months <= 0) return 0;
    let monthlyRate = rate / 12 / 100;
    if (monthlyRate === 0) return amount / months;
    let factor = Math.pow(1 + monthlyRate, months);
    let annuity = (monthlyRate * factor) / (factor - 1);
    return amount * annuity;
}

function updateCalculator() {
    let amount = parseFloat(document.getElementById('amount').value);
    let months = parseInt(document.getElementById('termMonths').value);
    let income = parseFloat(document.getElementById('income').value);

    if (isNaN(amount) || isNaN(months) || amount <= 0) {
        document.getElementById('monthlyPayment').innerText = '0 ₽';
        document.getElementById('debtLoad').innerHTML = '';
        return;
    }

    let payment = calculateMonthlyPayment(amount, months, avgRate);
    document.getElementById('monthlyPayment').innerText = Math.round(payment).toLocaleString() + ' ₽';

    if (income > 0) {
        let load = (payment / income) * 100;
        let loadClass = '';
        let warningText = '';

        if (load < 30) {
            loadClass = 'green';
            warningText = '✅ Низкая нагрузка. Хорошие шансы на одобрение.';
        } else if (load <= 50) {
            loadClass = 'yellow';
            warningText = '⚠️ Средняя нагрузка. Банки могут одобрить.';
        } else {
            loadClass = 'red';
            warningText = '🔴 Высокая нагрузка! Рекомендуем увеличить срок или уменьшить сумму.';
        }

        document.getElementById('debtLoad').innerHTML =
            '<span class="' + loadClass + '">Нагрузка на доход: ' + load.toFixed(1) + '%</span><br>' +
            '<span class="' + loadClass + '">' + warningText + '</span>';
    } else {
        document.getElementById('debtLoad').innerHTML = '<span class="warning">Укажите доход для расчета нагрузки</span>';
    }
}

document.addEventListener('DOMContentLoaded', function() {
    var amountEl = document.getElementById('amount');
    var termMonthsEl = document.getElementById('termMonths');
    var incomeEl = document.getElementById('income');
    
    if (amountEl) amountEl.addEventListener('input', updateCalculator);
    if (termMonthsEl) termMonthsEl.addEventListener('change', updateCalculator);
    if (incomeEl) incomeEl.addEventListener('input', updateCalculator);

    detectUserCity();
    checkAdminRole();
});

// ==================== ОПРЕДЕЛЕНИЕ ГОРОДА И ГЕОЛОКАЦИЯ ====================
async function detectUserCity() {
    try {
        const response = await fetch('https://api.ipify.org?format=json');
        const data = await response.json();
        const ip = data.ip;
        const cityResponse = await fetch(`/api/detect-city?ip=${ip}`);
        const city = await cityResponse.text();

        if (city && city !== 'неизвестно') {
            document.getElementById('userCity').innerText = city;
            localStorage.setItem('userCity', city);
        } else {
            document.getElementById('userCity').innerText = 'Москва (по умолчанию)';
        }
    } catch (error) {
        console.error('Ошибка определения города:', error);
        document.getElementById('userCity').innerText = 'Москва (по умолчанию)';
    }

    // Определяем геолокацию и сохраняем координаты
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var lat = position.coords.latitude;
            var lng = position.coords.longitude;
            localStorage.setItem('userLat', lat);
            localStorage.setItem('userLng', lng);
            console.log('Координаты сохранены:', lat, lng);
            document.getElementById('userCity').innerText = '📍 Местоположение определено';
        }, function(error) {
            console.log('Геолокация не разрешена или ошибка:', error);
        });
    }
}

// ==================== ПРОВЕРКА РОЛИ АДМИНА ====================
async function checkAdminRole() {
    try {
        const response = await fetch('/api/user-role');
        const role = await response.text();
        if (role === 'admin') {
            const adminLink = document.getElementById('adminLink');
            if (adminLink) {
                adminLink.style.display = 'inline';
                adminLink.href = '/admin/banks';
            }
        }
    } catch (error) {
        console.error('Ошибка проверки роли:', error);
    }
}
