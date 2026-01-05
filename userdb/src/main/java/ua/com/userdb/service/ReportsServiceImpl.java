package ua.com.userdb.service;

import ua.com.userdb.model.DBUser;
import ua.com.userdb.dao.DBUserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportsServiceImpl implements ReportsService {

    private final DBUserRepository userRepository;

    public ReportsServiceImpl(
    		DBUserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public List<DBUser> getReport(
            String type,
            Long databaseId,
            Long roleId,
            Long certificateTypeId,
            Long departmentId,
            Date expirationBefore
    ) {

        List<DBUser> users = userRepository.findAll();

        // ===== ФІЛЬТР ПО ПІДРОЗДІЛУ =====
        if (departmentId != null) {
            users = users.stream()
                    .filter(u -> u.getDepartment() != null &&
                            u.getDepartment().getId().equals(departmentId))
                    .collect(Collectors.toList());
        }

        switch (type) {

            case "ACCESS":
                return filterByAccess(users, databaseId);

            case "ROLE":
                return filterByRole(users, roleId);

            case "CERTIFICATE":
                return filterByCertificate(users, certificateTypeId);

            case "EXPIRING":
                return filterByExpiring(users, expirationBefore);

            default:
                return users;
        }
    }

    private List<DBUser> filterByAccess(List<DBUser> users, Long databaseId) {
        return users.stream()
                .filter(u -> u.getDbUserAccesses().stream()
                        .anyMatch(a ->
                                databaseId == null ||
                                a.getDatabase() != null &&
                                a.getDatabase().getId().equals(databaseId)
                        ))
                .collect(Collectors.toList());
    }

    private List<DBUser> filterByRole(List<DBUser> users, Long roleId) {
        return users.stream()
                .filter(u -> u.getDbUserRoles().stream()
                        .anyMatch(r ->
                                roleId == null ||
                                r.getDatabaseRole() != null &&
                                r.getDatabaseRole().getId().equals(roleId)
                        ))
                .collect(Collectors.toList());
    }

    private List<DBUser> filterByCertificate(List<DBUser> users, Long certTypeId) {
        return users.stream()
                .filter(u -> u.getDbUserCertificates().stream()
                        .anyMatch(c ->
                                certTypeId == null ||
                                c.getCertificateType() != null &&
                                c.getCertificateType().getId().equals(certTypeId)
                        ))
                .collect(Collectors.toList());
    }

    private List<DBUser> filterByExpiring(List<DBUser> users, Date expirationBefore) {

        if (expirationBefore == null) {
            return users;
        }

        return users.stream()
                .filter(u ->
                        u.getDbUserCertificates().stream()
                                .anyMatch(c ->
                                        c.getExpirationDate() != null &&
                                        c.getExpirationDate().before(expirationBefore)
                                )
                        ||
                        u.getDbUserAccesses().stream()
                                .anyMatch(a ->
                                        a.getAccessExpirationDate() != null &&
                                        a.getAccessExpirationDate().before(expirationBefore)
                                )
                )
                .collect(Collectors.toList());
    }
}
