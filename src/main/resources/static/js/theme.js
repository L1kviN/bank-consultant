(function() {
    // Синхронная проверка темы при загрузке страницы для избежания "мигания" (FOUC)
    var savedTheme = localStorage.getItem('theme');
    if (!savedTheme) {
        savedTheme = 'dark'; // по умолчанию темная тема
        localStorage.setItem('theme', savedTheme);
    }
    document.documentElement.setAttribute('data-theme', savedTheme);
})();

function toggleTheme() {
    var currentTheme = document.documentElement.getAttribute('data-theme');
    var newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    
    document.documentElement.setAttribute('data-theme', newTheme);
    localStorage.setItem('theme', newTheme);
    
    updateThemeIcon(newTheme);
}

function updateThemeIcon(theme) {
    var btn = document.getElementById('themeToggleBtn');
    if (btn) {
        // Если тема тёмная - предлагаем включить светлую (показываем солнце)
        btn.innerHTML = theme === 'dark' ? '☀️' : '🌙';
    }
}

document.addEventListener('DOMContentLoaded', function() {
    var theme = document.documentElement.getAttribute('data-theme');
    updateThemeIcon(theme);
    
    var btn = document.getElementById('themeToggleBtn');
    if (btn) {
        btn.addEventListener('click', toggleTheme);
    }
});
