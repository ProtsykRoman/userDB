package ua.com.userdb.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.userdb.model.DBUser;
import ua.com.userdb.service.CertificateTypeService;
import ua.com.userdb.service.DBUserService;
import ua.com.userdb.service.DatabaseRoleService;
import ua.com.userdb.service.DatabaseService;
import ua.com.userdb.service.DepartmentService;
import ua.com.userdb.service.ReportsService;

@Controller
@RequestMapping
public class ReportsController {
	
	private DatabaseService databaseService;
	private DatabaseRoleService databaseRoleService;
	private CertificateTypeService certificateTypeService;
	private DepartmentService departmentService;
	private ReportsService reportsService;
	private DBUserService dbUserService;
	
	public ReportsController(DatabaseService databaseService, DatabaseRoleService databaseRoleService,
			CertificateTypeService certificateTypeService, DepartmentService departmentService
			, ReportsService reportsService, DBUserService dbUserService) {
		this.databaseService = databaseService;
		this.databaseRoleService = databaseRoleService;
		this.certificateTypeService = certificateTypeService;
		this.departmentService = departmentService;
		this.reportsService = reportsService;
		this.dbUserService = dbUserService;
	}
	

	@GetMapping("/reports")
	public String reports(
	        @RequestParam(required = false) Long databaseId,
	        @RequestParam(required = false) Long roleId,
	        @RequestParam(required = false) Long certificateTypeId,
	        @RequestParam(required = false) Long departmentId,
	        @RequestParam(required = false) String expFrom,
	        @RequestParam(required = false) String expTo,
	        Model model) {

	    // Дані для селектів
	    model.addAttribute("databases", databaseService.findAllDatabase());
	    model.addAttribute("databaseRoles", databaseRoleService.findAllDatabaseRole());
	    model.addAttribute("certificateTypes", certificateTypeService.findAllCertificateType());
	    model.addAttribute("allDepartments", departmentService.getDepartmentsHierarchy());

	    // Перевірка: чи користувач натиснув "Показати"
	    boolean filtersUsed =
	            databaseId != null ||
	            roleId != null ||
	            certificateTypeId != null ||
	            departmentId != null ||
	            expFrom != null ||
	            expTo != null;

	    if (filtersUsed) {

	        // Конвертація дат
	        Date expFromDate = expFrom != null && !expFrom.isBlank() ? java.sql.Date.valueOf(expFrom) : null;
	        Date expToDate   = expTo != null && !expTo.isBlank()   ? java.sql.Date.valueOf(expTo)   : null;

	        List<DBUser> users = reportsService.getReport(
	                "custom",          // або видали якщо не потрібно
	                databaseId,
	                roleId,
	                certificateTypeId,
	                departmentId,
	                expToDate          // або заміни під твою логіку
	        );

	        model.addAttribute("users", users);
	    }

	    return "pages/reports/reports";
	}


	@GetMapping("/reports/results")
	public String reportResults(
	        @RequestParam String reportType,
	        @RequestParam(required = false) Long databaseId,
	        @RequestParam(required = false) Long roleId,
	        @RequestParam(required = false) Long certificateTypeId,
	        @RequestParam(required = false) Long departmentId,
	        @RequestParam(required = false) Date expirationBefore,
	        Model model) {

	    List<DBUser> results = reportsService.getReport(
	            reportType, databaseId, roleId, certificateTypeId, departmentId, expirationBefore
	    );

	    model.addAttribute("results", results);
	    return "pages/reports/results :: reportResults";
	}
	
}
