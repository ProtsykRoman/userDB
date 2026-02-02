package ua.com.userdb.service;

import java.util.List;

import ua.com.userdb.dto.ReportFilter;
import ua.com.userdb.dto.ReportRowDto;

public interface ReportsService {
	List<ReportRowDto> getReport(ReportFilter filter);
}
