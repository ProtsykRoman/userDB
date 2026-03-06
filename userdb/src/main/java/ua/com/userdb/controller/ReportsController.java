package ua.com.userdb.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.com.userdb.model.Department;
import ua.com.userdb.model.User;
import ua.com.userdb.model.DatabaseRole;
import ua.com.userdb.service.UserService;
import ua.com.userdb.dto.ReportFilter;
import ua.com.userdb.dto.ReportRowDto;
import ua.com.userdb.dto.ReportSourceType;
import ua.com.userdb.service.CertificateTypeService;
import ua.com.userdb.service.DatabaseRoleService;
import ua.com.userdb.service.DatabaseService;
import ua.com.userdb.service.DepartmentService;
import ua.com.userdb.service.ReportsService;
import ua.com.userdb.util.ReportExcelWriter;

@Controller
@RequestMapping("/reports")
public class ReportsController {

    private final ReportsService reportsService;
    private final UserService userService;
    private final DatabaseService databaseService;
    private final DatabaseRoleService databaseRoleService;
    private final CertificateTypeService certificateTypeService;
    private final DepartmentService departmentService;

    public ReportsController(
            ReportsService reportsService,
            UserService userService,
            DatabaseService databaseService,
            DatabaseRoleService databaseRoleService,
            CertificateTypeService certificateTypeService,
            DepartmentService departmentService
    ) {
        this.reportsService = reportsService;
        this.userService = userService;
        this.databaseService = databaseService;
        this.databaseRoleService = databaseRoleService;
        this.certificateTypeService = certificateTypeService;
        this.departmentService = departmentService;
    }

    // ====================== WEB REPORT ======================
    @GetMapping
    public String reports(
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer databaseId,
            @RequestParam(required = false) Integer databaseRoleId,
            @RequestParam(required = false) Integer certificateTypeId,
            @RequestParam(required = false) Boolean onlyDepartmentSelected,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationTo,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request,
            Model model
    ) {
        User currentUser = userService.findUserByUsername(principal.getUsername())
                .orElseThrow(() -> new IllegalStateException(
                    "Користувач не знайдений: " + principal.getUsername()));

        // Конвертація page у Integer з захистом від "null" або неправильного формату
        int pageNumber = page;

        // Нормалізація
        if (databaseId != null && databaseId <= 0) databaseId = null;
        if (databaseRoleId != null && databaseRoleId <= 0) databaseRoleId = null;
        if (certificateTypeId != null && certificateTypeId <= 0) certificateTypeId = null;

        if (onlyDepartmentSelected == null) {
            onlyDepartmentSelected =
                    departmentId != null &&
                    databaseId == null &&
                    databaseRoleId == null &&
                    certificateTypeId == null;
        }

        // Фільтр
        ReportFilter filter = new ReportFilter();
        filter.setDepartmentId(departmentId);
        filter.setDatabaseId(databaseId);
        filter.setDatabaseRoleId(databaseRoleId);
        filter.setCertificateTypeId(certificateTypeId);
        filter.setExpirationTo(expirationTo);
        filter.setOnlyDepartmentSelected(onlyDepartmentSelected);

        List<ReportRowDto> fullReport = reportsService.getReport(currentUser, filter);

        // Сторінкування
        int totalRecords = fullReport.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);
        int fromIndex = Math.min((pageNumber - 1) * size, totalRecords);
        int toIndex = Math.min(fromIndex + size, totalRecords);
        List<ReportRowDto> reportPage = fullReport.subList(fromIndex, toIndex);

        // Дозволені підрозділи
        List<Department> allowedDepartments = departmentService.getAllowedDepartmentsForUser(currentUser);

        // Атрибути моделі
        model.addAttribute("departments", allowedDepartments);
        model.addAttribute("databases", databaseService.findAllDatabase());

        List<DatabaseRole> sortedRoles = databaseRoleService.findAllDatabaseRole();
        sortedRoles.sort(java.util.Comparator.comparing((DatabaseRole r) -> r.getDatabase().getName())
                .thenComparing(DatabaseRole::getName));
        model.addAttribute("databaseRoles", sortedRoles);
        model.addAttribute("certificateTypes", certificateTypeService.findAllCertificateType());

        model.addAttribute("report", reportPage);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", totalPages);

        model.addAttribute("departmentId", departmentId);
        model.addAttribute("databaseId", databaseId);
        model.addAttribute("databaseRoleId", databaseRoleId);
        model.addAttribute("certificateTypeId", certificateTypeId);
        model.addAttribute("onlyDepartmentSelected", onlyDepartmentSelected);
        model.addAttribute("expirationTo", expirationTo);

        // Формуємо returnUrl без page=null
        String returnUrl = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null) {
            queryString = java.util.Arrays.stream(queryString.split("&"))
                    .filter(s -> !s.startsWith("page=") && !s.endsWith("=null"))
                    .reduce((a, b) -> a + "&" + b)
                    .orElse("");
            if (!queryString.isEmpty()) returnUrl += "?" + queryString;
        }
        model.addAttribute("returnUrl", returnUrl);

        return "pages/reports/reports";
    }

    // ====================== EXCEL EXPORT ======================
    @GetMapping("/export")
    public void exportToExcel(
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer databaseId,
            @RequestParam(required = false) Integer databaseRoleId,
            @RequestParam(required = false) Integer certificateTypeId,
            @RequestParam(required = false) Boolean onlyDepartmentSelected,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationTo,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
            HttpServletResponse response
    ) throws IOException {

        User currentUser = userService.findUserByUsername(principal.getUsername())
                .orElseThrow(() -> new IllegalStateException(
                        "Користувач не знайдений: " + principal.getUsername()));

        if (databaseId != null && databaseId <= 0) databaseId = null;
        if (databaseRoleId != null && databaseRoleId <= 0) databaseRoleId = null;
        if (certificateTypeId != null && certificateTypeId <= 0) certificateTypeId = null;

        if (onlyDepartmentSelected == null) {
            onlyDepartmentSelected =
                    departmentId != null &&
                    databaseId == null &&
                    databaseRoleId == null &&
                    certificateTypeId == null;
        }

        ReportFilter filter = new ReportFilter();
        filter.setDepartmentId(departmentId);
        filter.setDatabaseId(databaseId);
        filter.setDatabaseRoleId(databaseRoleId);
        filter.setCertificateTypeId(certificateTypeId);
        filter.setExpirationTo(expirationTo);
        filter.setOnlyDepartmentSelected(onlyDepartmentSelected);

        List<ReportRowDto> report = reportsService.getReport(currentUser, filter);

        boolean hasAccess = report.stream().anyMatch(r -> r.getSource() == ReportSourceType.ACCESS);
        boolean hasCertificate = report.stream().anyMatch(r -> r.getSource() == ReportSourceType.CERTIFICATE);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=report_" + LocalDate.now() + ".xlsx");

        ReportExcelWriter.writeReport(report, response.getOutputStream(), hasAccess, hasCertificate);
    }
}