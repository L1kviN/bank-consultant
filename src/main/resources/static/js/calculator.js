const AVG_RATE = 15.0;

function updateCalculator() {
    const amount = parseFloat(document.getElementById('amount').value);
    const months = parseInt(document.getElementById('termMonths').value);
    const income = parseFloat(document.getElementById('income').value);
    const display = document.getElementById('monthlyPayment');
    const loadInfo = document.getElementById('debtLoad');

    if (!amount || !months || amount <= 0) {
        display.innerText = '0 ₽';
        return;
    }

    const monthlyRate = AVG_RATE / 12 / 100;
    const factor = Math.pow(1 + monthlyRate, months);
    const payment = Math.round(amount * (monthlyRate * factor) / (factor - 1));

    display.innerText = payment.toLocaleString() + ' ₽';

    if (income > 0) {
        const load = (payment / income) * 100;
        let status = load < 30 ? 'green' : (load < 50 ? 'yellow' : 'red');
        loadInfo.innerHTML = `<span class="${status}">Нагрузка: ${load.toFixed(1)}%</span>`;
    }
}

// Слушатели событий
document.addEventListener('DOMContentLoaded', () => {
    const inputs = ['amount', 'termMonths', 'income'];
    inputs.forEach(id => {
        const el = document.getElementById(id);
        if (el) el.addEventListener('input', updateCalculator);
    });
});