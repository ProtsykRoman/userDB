document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');

    // --- 1. Додавання ДОСТУПІВ ---
    document.getElementById('addAccessBtn')?.addEventListener('click', () => {
        const body = document.getElementById('accessBody');
        const index = body.querySelectorAll('tr').length;
        const tr = document.createElement('tr');
        const options = databases.map(db => `<option value="${db.id}">${db.name}</option>`).join('');

        tr.innerHTML = `
            <td>
                <input type="hidden" name="dbUserAccesses[${index}].id" />
                <select name="dbUserAccesses[${index}].database.id" required>
                    <option value="">-- Виберіть базу даних --</option>${options}
                </select>
            </td>
            <td><input type="date" name="dbUserAccesses[${index}].accessExpirationDate" /></td>
            <td class="action-cell"><button type="button" class="btn-delete" onclick="removeAccessRow(this)">Видалити</button></td>
        `;
        body.appendChild(tr);
    });

    // --- 2. Додавання РОЛЕЙ ---
    document.getElementById('addRoleBtn')?.addEventListener('click', () => {
        const body = document.getElementById('roleBody');
        const index = body.querySelectorAll('tr').length;
        const tr = document.createElement('tr');
        const options = databaseRoles.map(r => `<option value="${r.id}">${r.name} (${r.database.name})</option>`).join('');

        tr.innerHTML = `
            <td>
                <input type="hidden" name="dbUserRoles[${index}].id" />
                <select name="dbUserRoles[${index}].databaseRole.id" required>
                    <option value="">-- Виберіть роль --</option>${options}
                </select>
            </td>
            <td class="action-cell"><button type="button" class="btn-delete" onclick="removeRoleRow(this)">Видалити</button></td>
        `;
        body.appendChild(tr);
    });

    // --- 3. Додавання СЕРТИФІКАТІВ ---
    document.getElementById('addCertBtn')?.addEventListener('click', () => {
        const body = document.getElementById('certBody');
        const index = body.querySelectorAll('tr').length;
        const tr = document.createElement('tr');
        const options = certificateTypes.map(c => `<option value="${c.id}">${c.name}</option>`).join('');

        tr.innerHTML = `
            <td>
                <input type="hidden" name="dbUserCertificates[${index}].id" />
                <select name="dbUserCertificates[${index}].certificateType.id" required>
                    <option value="">-- Виберіть тип сертифіката --</option>${options}
                </select>
            </td>
            <td><input type="text" name="dbUserCertificates[${index}].number"/></td>
            <td><input type="date" name="dbUserCertificates[${index}].expirationDate"/></td>
            <td><input type="checkbox" name="dbUserCertificates[${index}].blocked"/></td>
            <td class="action-cell"><button type="button" class="btn-delete" onclick="removeCertRow(this)">Видалити</button></td>
        `;
        body.appendChild(tr);
    });

    // --- 4. Глобальні функції видалення (викликаються з HTML onclick) ---
    window.removeAccessRow = function(btn) {
        btn.closest('tr').remove();
    };

    window.removeRoleRow = function(btn) {
        btn.closest('tr').remove();
    };

    window.removeCertRow = function(btn) {
        btn.closest('tr').remove();
    };

    // --- 5. Перерахунок індексів перед відправкою (Магія для Spring) ---
    form?.addEventListener('submit', function () {
        reindexTable('accessBody', 'dbUserAccesses');
        reindexTable('roleBody', 'dbUserRoles');
        reindexTable('certBody', 'dbUserCertificates');
    });

    function reindexTable(bodyId, arrayName) {
        const rows = document.querySelectorAll(`#${bodyId} tr`);
        rows.forEach((tr, index) => {
            tr.querySelectorAll('input, select').forEach(el => {
                const name = el.getAttribute('name');
                if (name) {
                    // Замінює будь-яке [число] на поточний порядковий номер [index]
                    const newName = name.replace(/\[\d+\]/, `[${index}]`);
                    el.setAttribute('name', newName);
                }
            });
        });
    }
});
