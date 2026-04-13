// Определяем город по IP и сохраняем координаты
async function initGlobalFeatures() {
    try {
        const ipRes = await fetch('https://api.ipify.org?format=json');
        const { ip } = await ipRes.json();
        const cityRes = await fetch(`/api/detect-city?ip=${ip}`);
        const city = await cityRes.text();

        const cityLabel = document.getElementById('userCity');
        if (cityLabel) cityLabel.innerText = city || 'Москва';
    } catch (e) {
        console.error("Ошибка детекции города");
    }

    // Проверка админа
    try {
        const res = await fetch('/api/user-role');
        const role = await res.text();
        const adminBtn = document.getElementById('adminLink');
        if (role === 'admin' && adminBtn) {
            adminBtn.style.display = 'inline';
            adminBtn.href = '/admin/banks';
        }
    } catch (e) {}
}

document.addEventListener('DOMContentLoaded', initGlobalFeatures);