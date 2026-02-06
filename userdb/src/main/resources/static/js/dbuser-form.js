document.addEventListener('DOMContentLoaded', function() {
    // 1. Отримуємо посилання на елементи
    const cert = document.getElementById('certificateTypeId');
    const db   = document.getElementById('databaseId');
    const role = document.getElementById('databaseRoleId');
    const form = document.querySelector('form');

    const allSelectors = [cert, db, role];

    /**
     * Функція логіки блокування
     */
    function toggleFilters() {
        // Перевіряємо, чи всі елементи існують на сторінці
        if (!cert || !db || !role) return;

        // Знаходимо селект, у якого вибрано значення (не порожнє)
        const activeSelect = allSelectors.find(select => select.value !== "");

        if (activeSelect) {
            // Якщо щось вибрано — блокуємо всі інші, крім активного
            allSelectors.forEach(select => {
                if (select !== activeSelect) {
                    select.disabled = true;
                } else {
                    select.disabled = false;
                }
            });
        } else {
            // Якщо нічого не вибрано — розблокуємо все
            allSelectors.forEach(select => {
                select.disabled = false;
            });
        }
    }

    // 2. Навішуємо слухачі подій на кожну зміну
    allSelectors.forEach(select => {
        if (select) {
            select.addEventListener('change', toggleFilters);
        }
    });

    // 3. Викликаємо функцію одразу при завантаженні 
    // (потрібно, якщо сторінка відкрилася вже з вибраними фільтрами від Thymeleaf)
    toggleFilters();

    // 4. ВАЖЛИВО: Перед відправкою форми розблоковуємо поля.
    // Заблоковані (disabled) поля браузер не відправляє на сервер, 
    // тому їх треба "включити" за мілісекунду до відправки.
    if (form) {
        form.addEventListener('submit', function() {
            allSelectors.forEach(select => {
                if (select) select.disabled = false;
            });
        });
    }
});
