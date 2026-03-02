package ua.com.userdb.service;

import ua.com.userdb.dao.ReportsRepository;
import ua.com.userdb.dto.AggregatedRow;
import ua.com.userdb.dto.ReportFilter;
import ua.com.userdb.dto.ReportRowDto;
import ua.com.userdb.dto.ReportSourceType;
import ua.com.userdb.model.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReportsServiceImpl implements ReportsService {

    private final ReportsRepository reportsRepository;
    private final DepartmentService departmentService;
    private final ReportDepartmentAccessService accessService;

    public ReportsServiceImpl(ReportsRepository reportsRepository, DepartmentService departmentService,
    		ReportDepartmentAccessService accessService) {
        this.reportsRepository = reportsRepository;
        this.departmentService = departmentService;
        this.accessService = accessService;
    }

    @Override
    public List<ReportRowDto> getReport(User currentUser, ReportFilter filter) {

        List<ReportRowDto> result = new ArrayList<>();
        boolean hasDate = filter.getExpirationTo() != null;

        // ================= ПІДРОЗДІЛИ =================
        List<Integer> departmentIds =
                accessService.resolveDepartmentIds(currentUser, filter, departmentService);

        // ================= CERTIFICATES =================
        if (filter.getCertificateTypeId() != null) {
            var rows = hasDate
                    ? reportsRepository.findCertificatesWithDate(departmentIds, filter.getCertificateTypeId(), filter.getExpirationTo())
                    : reportsRepository.findCertificatesNoDate(departmentIds, filter.getCertificateTypeId());

            rows.forEach(r -> result.add(new ReportRowDto(r, ReportSourceType.CERTIFICATE)));
            return sort(result);
        }

        // ================= ACCESS BY ROLE =================
        if (filter.getDatabaseRoleId() != null) {
            var rows = hasDate
                    ? reportsRepository.findAccessesByRoleWithDate(departmentIds, filter.getDatabaseRoleId(), filter.getExpirationTo())
                    : reportsRepository.findAccessesByRoleNoDate(departmentIds, filter.getDatabaseRoleId());

            rows.forEach(r -> result.add(new ReportRowDto(r, ReportSourceType.ACCESS)));
            return sort(aggregateAccessRows(result));
        }

        // ================= ACCESS BY DATABASE =================
        if (filter.getDatabaseId() != null) {
            var rows = hasDate
                    ? reportsRepository.findAccessesByDatabaseWithDate(departmentIds, filter.getDatabaseId(), filter.getExpirationTo())
                    : reportsRepository.findAccessesByDatabaseNoDate(departmentIds, filter.getDatabaseId());

            rows.forEach(r -> result.add(new ReportRowDto(r, ReportSourceType.ACCESS)));
            return sort(aggregateAccessRows(result));
        }

        // ================= ONLY DEPARTMENT / ПІДРОЗДІЛИ =================
        if (departmentIds != null) {
            var rows = hasDate
                    ? reportsRepository.findAccessesByDepartmentsWithDate(departmentIds, filter.getExpirationTo())
                    : reportsRepository.findAccessesByDepartmentsNoDate(departmentIds);

            rows.forEach(r -> result.add(new ReportRowDto(r, ReportSourceType.ACCESS)));
            return sort(aggregateAccessRows(result));
        }

        // ================= DEFAULT: CERTIFICATES =================
        var rows = hasDate
                ? reportsRepository.findCertificatesWithDate(departmentIds, null, filter.getExpirationTo())
                : reportsRepository.findCertificatesNoDate(departmentIds, null);

        rows.forEach(r -> result.add(new ReportRowDto(r, ReportSourceType.CERTIFICATE)));
        return sort(result);
    }

    private List<ReportRowDto> sort(List<ReportRowDto> list) {
    	List<ReportRowDto> mutable = new ArrayList<>(list);
        mutable.sort(
            Comparator.comparing(ReportRowDto::getUserId)
                      .thenComparing(
                          ReportRowDto::getExpirationDate, 
                          Comparator.nullsLast(Comparator.naturalOrder())
                      )
        );
        return mutable;
    }
    
    private List<ReportRowDto> aggregateAccessRows(List<ReportRowDto> rows) {

        Map<String, AggregatedRow> map = new LinkedHashMap<>();

        for (ReportRowDto row : rows) {

            // ключ = user + database
            String key = row.getUserId() + "|" + row.getDatabaseName();

            map.putIfAbsent(key, new AggregatedRow(row));

            AggregatedRow agg = map.get(key);

            if (row.getDatabaseRoleName() != null) {
            	agg.addRole(row.getDatabaseRoleName());
            }
        }

        return map.values().stream()
                .map(AggregatedRow::toDto)
                .toList();
    }
    
    
}
