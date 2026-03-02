package ua.com.userdb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ua.com.userdb.dto.ReportFilter;
import ua.com.userdb.model.Role;
import ua.com.userdb.model.User;

@Service
public class ReportDepartmentAccessService {

    public List<Integer> resolveDepartmentIds(
            User currentUser,
            ReportFilter filter,
            DepartmentService departmentService
    ) {

        Integer requestedDepartmentId = filter.getDepartmentId();

        // ================= ADMIN =================
        if (currentUser.getRole() == Role.ADMIN) {

            if (requestedDepartmentId == null) {
                return null; // без обмежень
            }

            if (filter.isOnlyDepartmentSelected()) {
                return List.of(requestedDepartmentId);
            }

            return departmentService.getSubDepartmentIds(requestedDepartmentId);
        }

        // ================= USER =================

        // дозволені департаменти користувача
        List<Integer> allowedIds =
                departmentService.getSubDepartmentIds(
                        currentUser.getDepartment().getId()
                );

        // якщо користувач нічого не вибрав → показуємо весь його діапазон
        if (requestedDepartmentId == null) {
            return allowedIds;
        }

        // якщо вибрав департамент — перевіряємо, чи він дозволений
        if (!allowedIds.contains(requestedDepartmentId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Недостатньо прав для перегляду цього департаменту"
            );
        }

        if (filter.isOnlyDepartmentSelected()) {
            return List.of(requestedDepartmentId);
        }

        return departmentService.getSubDepartmentIds(requestedDepartmentId);
    }
}
