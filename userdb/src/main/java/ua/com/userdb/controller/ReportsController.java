package ua.com.userdb.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import ua.com.userdb.dto.ReportFilter;
import ua.com.userdb.dto.ReportRowDto;
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
    private final DatabaseService databaseService;
    private final DatabaseRoleService databaseRoleService;
    private final CertificateTypeService certificateTypeService;
    private final DepartmentService departmentService;

    public ReportsController(
            ReportsService reportsService,
            DatabaseService databaseService,
            DatabaseRoleService databaseRoleService,
            CertificateTypeService certificateTypeService,
            DepartmentService departmentService
    ) {
        this.reportsService = reportsService;
        this.databaseService = databaseService;
        this.databaseRoleService = databaseRoleService;
        this.certificateTypeService = certificateTypeService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String reports(
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer databaseId,
            @RequestParam(required = false) Integer databaseRoleId,
            @RequestParam(required = false) Integer certificateTypeId,
            @RequestParam(required = false) Boolean onlyDepartmentSelected,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationTo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model
    ) {
        // нормалізація значень
        if (databaseId != null && databaseId <= 0) databaseId = null;
        if (databaseRoleId != null && databaseRoleId <= 0) databaseRoleId = null;
        if (certificateTypeId != null && certificateTypeId <= 0) certificateTypeId = null;

        // якщо параметр не переданий, визначаємо його автоматично
        if (onlyDepartmentSelected == null) {
            onlyDepartmentSelected =
                    departmentId != null &&
                    databaseId == null &&
                    databaseRoleId == null &&
                    certificateTypeId == null;
        }

        // додаємо атрибути для фільтрів у модель
        model.addAttribute("departments", departmentService.getDepartmentsHierarchy());
        model.addAttribute("databases", databaseService.findAllDatabase());
        model.addAttribute("databaseRoles", databaseRoleService.findAllDatabaseRole());
        model.addAttribute("certificateTypes", certificateTypeService.findAllCertificateType());

        model.addAttribute("departmentId", departmentId);
        model.addAttribute("databaseId", databaseId);
        model.addAttribute("databaseRoleId", databaseRoleId);
        model.addAttribute("certificateTypeId", certificateTypeId);
        model.addAttribute("onlyDepartmentSelected", onlyDepartmentSelected);

        boolean filtersUsed =
                departmentId != null ||
                databaseId != null ||
                databaseRoleId != null ||
                certificateTypeId != null ||
                expirationTo != null;

        if (filtersUsed) {
            ReportFilter filter = new ReportFilter();
            filter.setDepartmentId(departmentId);
            filter.setDatabaseId(databaseId);
            filter.setDatabaseRoleId(databaseRoleId);
            filter.setCertificateTypeId(certificateTypeId);
            filter.setExpirationTo(expirationTo);
            filter.setOnlyDepartmentSelected(onlyDepartmentSelected);

            List<ReportRowDto> fullReport = reportsService.getReport(filter);

            int totalRecords = fullReport.size();
            int totalPages = (int) Math.ceil((double) totalRecords / size);

            int fromIndex = Math.min((page - 1) * size, totalRecords);
            int toIndex = Math.min(fromIndex + size, totalRecords);
            List<ReportRowDto> reportPage = fullReport.subList(fromIndex, toIndex);

            model.addAttribute("report", reportPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("totalRecords", totalRecords);
            model.addAttribute("totalPages", totalPages);
        }

        return "pages/reports/reports";
    }

    @GetMapping("/export")
    public void exportToExcel(
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer databaseId,
            @RequestParam(required = false) Integer databaseRoleId,
            @RequestParam(required = false) Integer certificateTypeId,
            @RequestParam(required = false) Boolean onlyDepartmentSelected,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationTo,
            HttpServletResponse response
    ) throws IOException {

        // 1. Нормалізація значень (прибираємо "всі", якщо прийшло 0 або -1)
        if (databaseId != null && databaseId <= 0) databaseId = null;
        if (databaseRoleId != null && databaseRoleId <= 0) databaseRoleId = null;
        if (certificateTypeId != null && certificateTypeId <= 0) certificateTypeId = null;

        // 2. Визначення логіки області пошуку (як у методі звіту)
        if (onlyDepartmentSelected == null) {
            onlyDepartmentSelected = (departmentId != null && databaseId == null && 
                                     databaseRoleId == null && certificateTypeId == null);
        }

        // 3. Підготовка фільтра для отримання даних
        ReportFilter filter = new ReportFilter();
        filter.setDepartmentId(departmentId);
        filter.setDatabaseId(databaseId);
        filter.setDatabaseRoleId(databaseRoleId);
        filter.setCertificateTypeId(certificateTypeId);
        filter.setExpirationTo(expirationTo);
        filter.setOnlyDepartmentSelected(onlyDepartmentSelected);

        // 4. Отримання даних із сервісу
        List<ReportRowDto> report = reportsService.getReport(filter);

        // 5. ЛОГІКА ДИНАМІЧНИХ КОЛОНОК ДЛЯ EXCEL
        // Показуємо Базу та Роль, якщо вибрано хоча б один із цих фільтрів
        boolean showDbAndRole = (databaseId != null || databaseRoleId != null);
        
        // Показуємо Сертифікат, якщо вибрано його фільтр 
        // АБО якщо взагалі нічого не вибрано (бо сервіс за дефолтом вертає сертифікати)
        boolean showCert = (certificateTypeId != null) || (!showDbAndRole && certificateTypeId == null);

        // 6. Налаштування HTTP-відповіді
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=report_" + LocalDate.now() + ".xlsx");

        // 7. Виклик врайтера з нашими параметрами
        // Параметри відповідно до підпису методу: (rows, outputStream, includeAccess, includeCert)
        ReportExcelWriter.writeReport(
                report, 
                response.getOutputStream(), 
                showDbAndRole, 
                showCert
        );
    }

}
