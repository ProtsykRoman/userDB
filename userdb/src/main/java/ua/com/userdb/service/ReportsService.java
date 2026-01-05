package ua.com.userdb.service;

import java.util.Date;
import java.util.List;

import ua.com.userdb.model.DBUser;

public interface ReportsService {
	List<DBUser> getReport(
            String reportType,
            Long databaseId,
            Long roleId,
            Long certificateTypeId,
            Long departmentId,
            Date expirationBefore
    );
}
