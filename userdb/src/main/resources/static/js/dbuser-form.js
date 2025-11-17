document.addEventListener('DOMContentLoaded', function() {

    // --- Доступи ---
    const accessBody = document.getElementById('accessBody');
    const addAccessBtn = document.getElementById('addAccessBtn');

    addAccessBtn?.addEventListener('click', () => {
        const index = accessBody.querySelectorAll('tr').length;
        const tr = document.createElement('tr');

        const options = databases.map(db => `<option value="${db.id}">${db.name}</option>`).join('');

        tr.innerHTML = `
            <td>
                <input type="hidden" name="dbUserAccesses[${index}].id" />
                <select name="dbUserAccesses[${index}].database.id" required>
                    <option value="">-- Виберіть базу даних --</option>${options}
                </select>
            </td>
            <td>
                <input type="date" name="dbUserAccesses[${index}].accessExpirationDate" />
            </td>
            <td>
                <button type="button" class="btn-delete">Видалити</button>
            </td>
        `;
        tr.querySelector('.btn-delete').addEventListener('click', () => removeAccessRow(tr));
        accessBody.appendChild(tr);
    });

    // --- Ролі ---
    const roleBody = document.getElementById('roleBody');
    const addRoleBtn = document.getElementById('addRoleBtn');

    addRoleBtn?.addEventListener('click', () => {
        const index = roleBody.querySelectorAll('tr').length;
        const tr = document.createElement('tr');
        const options = databaseRoles.map(r => `<option value="${r.id}">${r.name} (${r.database.name})</option>`).join('');

        tr.innerHTML = `
            <td>
                <input type="hidden" name="dbUserRoles[${index}].id" />
                <select name="dbUserRoles[${index}].databaseRole.id" required>
                    <option value="">-- Виберіть роль --</option>${options}
                </select>
            </td>
            <td><button type="button" class="btn-delete">Видалити</button></td>
        `;
        tr.querySelector('.btn-delete').addEventListener('click', () => removeRoleRow(tr));
        roleBody.appendChild(tr);
    });

    // --- Сертифікати ---
    const certBody = document.getElementById('certBody');
    const addCertBtn = document.getElementById('addCertBtn');

    addCertBtn?.addEventListener('click', () => {
        const index = certBody.querySelectorAll('tr').length;
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
            <td><input type="checkbox" name="dbUserCertificates[${index}].isBlocked"/></td>
            <td><button type="button" class="btn-delete">Видалити</button></td>
        `;
        tr.querySelector('.btn-delete').addEventListener('click', () => removeCertRow(tr));
        certBody.appendChild(tr);
    });
});

// --- Функції видалення ---
function removeAccessRow(tr) {
    tr.remove();
    updateIndices('accessBody', 'dbUserAccesses');
}

function removeRoleRow(tr) {
    tr.remove();
    updateIndices('roleBody', 'dbUserRoles');
}

function removeCertRow(tr) {
    tr.remove();
    updateIndices('certBody', 'dbUserCertificates');
}

// --- Оновлення індексів ---
function updateIndices(tbodyId, arrayName) {
    const tbody = document.getElementById(tbodyId);
    Array.from(tbody.querySelectorAll('tr')).forEach((tr, idx) => {
        tr.querySelectorAll('input, select').forEach(el => {
            const name = el.getAttribute('name');
            if (name) el.setAttribute('name', name.replace(/\[\d+\]/, `[${idx}]`));
        });
    });
}
