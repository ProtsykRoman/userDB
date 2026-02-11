document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('userForm');
    const checkbox = document.getElementById('changePassword');
    const passwordFields = document.getElementById('passwordFields');
    const userIdInput = document.getElementById('id');

    function toggleFields() {
        if (passwordFields && checkbox) {
            passwordFields.style.display = checkbox.checked ? 'block' : 'none';
        }
    }

    if (checkbox) {
        checkbox.addEventListener('change', toggleFields);
        toggleFields(); // Перевірка при завантаженні
    }

    form?.addEventListener('submit', function(event) {
        const userId = parseInt(userIdInput.value) || 0;
        let p1, p2;

        if (userId === 0) {
            p1 = document.getElementById('password').value;
            p2 = document.getElementById('confirmPassword').value;
        } else if (checkbox && checkbox.checked) {
            p1 = document.getElementById('newPassword').value;
            p2 = document.getElementById('confirmPasswordEdit').value;
        } else {
            return true; // Не змінюємо пароль
        }

        if (p1 !== p2) {
            alert("Паролі не збігаються!");
            event.preventDefault();
            return false;
        }
    });
});
