async function updateAllCoordinates() {
    const resultDiv = document.getElementById('batchResult');
    resultDiv.innerHTML = '<div class="result-message">⏳ Обработка... Это может занять несколько минут</div>';

    try {
        const response = await fetch('/admin/update-coordinates', { method: 'POST' });
        const result = await response.json();
        resultDiv.innerHTML = `<div class="result-message result-success">✅ ${result.message} (успешно: ${result.success}, ошибок: ${result.failed})</div>`;
        setTimeout(() => location.reload(), 2000);
    } catch (error) {
        resultDiv.innerHTML = `<div class="result-message result-error">❌ Ошибка: ${error.message}</div>`;
    }
}

async function searchBanks() {
    const query = document.getElementById('searchInput').value;
    const city = document.getElementById('cityInput').value;

    if (!query) return;

    const resultsDiv = document.getElementById('searchResults');
    resultsDiv.innerHTML = '<div class="result-message">⏳ Поиск...</div>';

    try {
        const response = await fetch(`/admin/search-banks-api?query=${encodeURIComponent(query)}&city=${encodeURIComponent(city)}`, {
            method: 'POST'
        });
        const data = await response.json();

        resultsDiv.innerHTML = '<hr><h4 style="margin-bottom: 12px; font-size: 16px; color: var(--text-primary);">📋 Результаты поиска:</h4>';

        if (data.suggestions && data.suggestions.length > 0) {
            data.suggestions.forEach(bank => {
                const address = bank.data.address ? bank.data.address.value : '—';
                resultsDiv.innerHTML += `
                        <div class="bank-item">
                            <div class="bank-name">${bank.value}</div>
                            <div><strong style="color: var(--text-muted)">БИК:</strong> ${bank.data.bic || '—'}</div>
                            <div><strong style="color: var(--text-muted)">ИНН:</strong> ${bank.data.inn || '—'}</div>
                            <div><strong style="color: var(--text-muted)">КПП:</strong> ${bank.data.kpp || '—'}</div>
                            <div><strong style="color: var(--text-muted)">Корсчет:</strong> ${bank.data.correspondent_account || '—'}</div>
                            <div><strong style="color: var(--text-muted)">Адрес:</strong> ${address}</div>
                            <div><strong style="color: var(--text-muted)">Статус:</strong> ${bank.data.state ? bank.data.state.status : '—'}</div>
                        </div>
                    `;
            });
        } else {
            resultsDiv.innerHTML += '<div class="result-message">Ничего не найдено</div>';
        }
    } catch (error) {
        resultsDiv.innerHTML = `<div class="result-message result-error">❌ Ошибка поиска: ${error.message}</div>`;
    }
}
