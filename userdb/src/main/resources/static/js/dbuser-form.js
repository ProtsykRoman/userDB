<script th:inline="javascript">
    const databases = /*[[${databases}]]*/ [];
    const accessBody = document.getElementById('accessBody');
    const addAccessBtn = document.getElementById('addAccessBtn');

    addAccessBtn.addEventListener('click', () => {
        const index = accessBody.querySelectorAll('tr').length;
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td>
                <select name="dbUserAccesses[${index}].database.id" required>
                    <option value="">-- Виберіть базу даних --</option>
                    ${databases.map(db => `<option value="${db.id}">${db.name}</option>`).join('')}
                </select>
            </td>
            <td>
                <input type="date" name="dbUserAccesses[${index}].accessExpirationDate"/>
            </td>
            <td>
                <button type="button" class="btn-delete" onclick="removeAccessRow(this)">Видалити</button>
            </td>
        `;
        accessBody.appendChild(tr);
    });

    function removeAccessRow(button) {
        button.closest('tr').remove();
        // після видалення — оновити name-індекси
        Array.from(accessBody.querySelectorAll('tr')).forEach((tr, idx) => {
            tr.querySelectorAll('select, input').forEach(el => {
                el.name = el.name.replace(/\[\d+\]/, `[${idx}]`);
            });
        });
    }
</script>
