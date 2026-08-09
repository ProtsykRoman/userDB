document.addEventListener("DOMContentLoaded", function () {

    // =====================================================
    // DATABASE MODAL
    // =====================================================

    const dbModal = document.getElementById("dbModal");
    const openDbModal = document.getElementById("openDbModal");
    const applyDbSelection = document.getElementById("applyDbSelection");
    const clearDbSelection = document.getElementById("clearDbSelection");
    const dbSelectedText = document.getElementById("dbSelectedText");

    // =====================================================
    // ROLES MODAL
    // =====================================================

    const roleModal = document.getElementById("roleModal");
    const openRoleModal = document.getElementById("openRoleModal");
    const applyRoleSelection = document.getElementById("applyRoleSelection");
    const clearRoleSelection = document.getElementById("clearRoleSelection");
    const roleSelectedText = document.getElementById("roleSelectedText");
    const roleSearch = document.getElementById("roleSearch");

    // =====================================================
    // DATABASE CAPTION
    // =====================================================

    function updateDbCaption() {

        const checked =
            document.querySelectorAll(".db-checkbox:checked");

        if (checked.length === 0) {
            dbSelectedText.textContent = "Всі бази";
            return;
        }

        if (checked.length <= 3) {

            dbSelectedText.textContent =
                [...checked]
                    .map(cb =>
                        cb.parentElement.innerText.trim())
                    .join(", ");

            return;
        }

        dbSelectedText.textContent =
            "Вибрано баз: " + checked.length;
    }

    // =====================================================
    // FILTER ROLES BY DATABASE
    // =====================================================

	function filterRolesByDatabase() {

	    const selectedDatabases =
	        [...document.querySelectorAll(".db-checkbox:checked")]
	            .map(cb => cb.value);

	    document
	        .querySelectorAll(".role-item")
	        .forEach(role => {

	            const roleDbId = role.dataset.dbId;

	            const visible =
	                selectedDatabases.length === 0 ||
	                selectedDatabases.includes(roleDbId);

	            role.style.display =
	                visible ? "flex" : "none";

	            if (!visible) {

	                const checkbox =
	                    role.querySelector(".role-checkbox");

	                if (checkbox) {
	                    checkbox.checked = false;
	                }
	            }
	        });

	    updateRoleCaption();
	}

    // =====================================================
    // ROLE CAPTION
    // =====================================================

    function updateRoleCaption() {

        const checked =
            document.querySelectorAll(".role-checkbox:checked");

        if (checked.length === 0) {

            roleSelectedText.textContent = "Всі ролі";
            return;
        }

        if (checked.length <= 3) {

            roleSelectedText.textContent =
                [...checked]
                    .map(cb =>
                        cb.parentElement.innerText.trim())
                    .join(", ");

            return;
        }

        roleSelectedText.textContent =
            "Вибрано ролей: " + checked.length;
    }

    // =====================================================
    // OPEN DATABASE MODAL
    // =====================================================

    if (openDbModal) {

        openDbModal.addEventListener("click", function () {

            dbModal.classList.add("show");

        });
    }

    // =====================================================
    // APPLY DATABASES
    // =====================================================

	if (applyDbSelection) {

	    applyDbSelection.addEventListener("click", function () {

	        updateDbCaption();

	        filterRolesByDatabase();

	        updateRoleCaption();

	        dbModal.classList.remove("show");

	    });
	}

    // =====================================================
    // CLEAR DATABASES
    // =====================================================

    if (clearDbSelection) {

        clearDbSelection.addEventListener("click", function () {

            document
                .querySelectorAll(".db-checkbox")
                .forEach(cb => cb.checked = false);

				updateDbCaption();
				filterRolesByDatabase();
				updateRoleCaption();

        });
    }

    // =====================================================
    // CLOSE DB MODAL BY BACKGROUND
    // =====================================================

    if (dbModal) {

        dbModal.addEventListener("click", function (e) {

            if (e.target === dbModal) {

                dbModal.classList.remove("show");

            }
        });
    }

    // =====================================================
    // OPEN ROLE MODAL
    // =====================================================

    if (openRoleModal) {

        openRoleModal.addEventListener("click", function () {

            roleModal.classList.add("show");

        });
    }

    // =====================================================
    // APPLY ROLES
    // =====================================================

    if (applyRoleSelection) {

        applyRoleSelection.addEventListener("click", function () {

            updateRoleCaption();

            roleModal.classList.remove("show");

        });
    }

    // =====================================================
    // CLEAR ROLES
    // =====================================================

    if (clearRoleSelection) {

        clearRoleSelection.addEventListener("click", function () {

            document
                .querySelectorAll(".role-checkbox")
                .forEach(cb => cb.checked = false);

            updateRoleCaption();

        });
    }

    // =====================================================
    // CLOSE ROLE MODAL BY BACKGROUND
    // =====================================================

    if (roleModal) {

        roleModal.addEventListener("click", function (e) {

            if (e.target === roleModal) {

                roleModal.classList.remove("show");

            }
        });
    }

    // =====================================================
    // ROLE SEARCH
    // =====================================================

    if (roleSearch) {

        roleSearch.addEventListener("keyup", function () {

            const value =
                this.value.toLowerCase();

            document
                .querySelectorAll(".role-item")
                .forEach(item => {

                    if (item.classList.contains("hidden")) {
                        return;
                    }

                    const text =
                        item.innerText.toLowerCase();

                    item.style.display =
                        text.includes(value)
                            ? "flex"
                            : "none";
                });
        });
    }

    // =====================================================
    // ROLE EVENTS
    // =====================================================

    document
        .querySelectorAll(".role-checkbox")
        .forEach(cb => {

            cb.addEventListener(
                "change",
                updateRoleCaption
            );

        });

    // =====================================================
    // INIT
    // =====================================================

    updateDbCaption();
    updateRoleCaption();
    filterRolesByDatabase();

});