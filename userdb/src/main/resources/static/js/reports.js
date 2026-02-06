document.addEventListener("DOMContentLoaded", function () {

    const cert = document.getElementById('certificateTypeId');
    const db   = document.getElementById('databaseId');
    const role = document.getElementById('databaseRoleId');

    if (!cert || !db || !role) return;

    function toggleFilters() {

        cert.disabled = false;
        db.disabled   = false;
        role.disabled = false;

        if (cert.value !== "") {
            db.disabled   = true;
            role.disabled = true;
        }
        else if (db.value !== "") {
            cert.disabled = true;
            role.disabled = true;
        }
        else if (role.value !== "") {
            cert.disabled = true;
            db.disabled   = true;
        }
    }

    cert.addEventListener('change', toggleFilters);
    db.addEventListener('change', toggleFilters);
    role.addEventListener('change', toggleFilters);

    toggleFilters();

    const form = document.querySelector('form');

    if (form) {
        form.addEventListener('submit', function () {
            cert.disabled = false;
            db.disabled   = false;
            role.disabled = false;
        });
    }

});
