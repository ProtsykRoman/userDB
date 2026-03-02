package ua.com.userdb.service;

import java.util.List;

import ua.com.userdb.dto.ReportFilter;
import ua.com.userdb.dto.ReportRowDto;
import ua.com.userdb.model.User;

public interface ReportsService {
	List<ReportRowDto> getReport(User currentUser, ReportFilter filter);
}
