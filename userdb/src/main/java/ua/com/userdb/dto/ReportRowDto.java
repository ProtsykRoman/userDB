package ua.com.userdb.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportRowDto {

	private Integer userId;
    private Integer identificationNumber;
    private String userName;
    private String departmentName;
    private Boolean isActive;

    private String databaseName;
    private String databaseRoleName;
    private String certificateTypeName;

    private LocalDate expirationDate;
    private ReportSourceType source;

    // JPQL constructor
    public ReportRowDto(
    		Integer userId,
            Integer identificationNumber,
            String userName,
            String departmentName,
            Boolean isActive,
            String databaseName,
            String databaseRoleName,
            String certificateTypeName,
            LocalDate expirationDate
    ) {
    	this.userId = userId;
    	this.identificationNumber = identificationNumber;
        this.userName = userName;
        this.departmentName = departmentName;
        this.isActive = isActive;
        this.databaseName = databaseName;
        this.databaseRoleName = databaseRoleName;
        this.certificateTypeName = certificateTypeName;
        this.expirationDate = expirationDate;
    }

    // Constructor for service conversion
    public ReportRowDto(ReportRowDto row, ReportSourceType source) {
    	this.userId = row.userId;
        this.identificationNumber = row.identificationNumber;
        this.userName = row.userName;
        this.departmentName = row.departmentName;
        this.isActive = row.isActive;
        this.databaseName = row.databaseName;
        this.databaseRoleName = row.databaseRoleName;
        this.certificateTypeName = row.certificateTypeName;
        this.expirationDate = row.expirationDate;
        this.source = source;
    }
}
