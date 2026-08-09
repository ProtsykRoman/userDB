package ua.com.userdb.dto;

import java.time.LocalDate;
import java.util.List;

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
	private List<Integer> databaseIds;
	private List<Integer> databaseRoleIds;
    private Integer certificateTypeId;
    private LocalDate expirationTo;
    private boolean onlyDepartmentSelected;
}


