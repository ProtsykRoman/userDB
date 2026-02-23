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
	private Long identificationNumber;
	private String userName;
	private String departmentName;
	private Boolean isActive;

	private String databaseName;
	private String databaseRoleName;
	private String certificateTypeName;
	private String certificateNumber;

	private LocalDate expirationDate;
	private ReportSourceType source;

	// JPQL constructor
	public ReportRowDto(Integer userId, Long identificationNumber, String userName, String departmentName,
			Boolean isActive, String databaseName, String databaseRoleName, String certificateTypeName,
			String certificateNumber, LocalDate expirationDate, ReportSourceType source) {
		this.userId = userId;
		this.identificationNumber = identificationNumber;
		this.userName = userName;
		this.departmentName = departmentName;
		this.isActive = isActive;
		this.databaseName = databaseName;
		this.databaseRoleName = databaseRoleName;
		this.certificateTypeName = certificateTypeName;
		this.certificateNumber = certificateNumber;
		this.expirationDate = expirationDate;
		this.source = source;
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
		this.certificateNumber = row.certificateNumber;
		this.expirationDate = row.expirationDate;
		this.source = source;
	}
}
