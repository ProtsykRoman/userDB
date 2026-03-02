package ua.com.userdb.dto;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

public class AggregatedRow {

	private final Integer userId;
    private final Long identificationNumber;
    private final String userName;
    private final String departmentName;
    private final Boolean isActive;
    private final String databaseName;
    private final LocalDate expirationDate;

    private final Set<String> roles = new LinkedHashSet<>();

    public AggregatedRow(ReportRowDto row) {
        this.userId = row.getUserId();
        this.identificationNumber = row.getIdentificationNumber();
        this.userName = row.getUserName();
        this.departmentName = row.getDepartmentName();
        this.isActive = row.getIsActive();
        this.databaseName = row.getDatabaseName();
        this.expirationDate = row.getExpirationDate();
    }

    public ReportRowDto toDto() {
        return new ReportRowDto(
                userId,
                identificationNumber,
                userName,
                departmentName,
                isActive,
                databaseName,
                String.join(", ", roles),
                null,
                null,
                expirationDate,
                ReportSourceType.ACCESS
        );
    }
    
    public void addRole(String roleName) {
        if (roleName != null) {
            roles.add(roleName);
        }
    }
    
}
