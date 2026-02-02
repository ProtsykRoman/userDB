package ua.com.userdb.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportFilter {
	private Integer departmentId;
    private Integer databaseId;
    private Integer databaseRoleId;
    private Integer certificateTypeId;
    private LocalDate expirationTo;
}
