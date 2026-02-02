package ua.com.userdb.service;

import ua.com.userdb.dao.ReportsRepository;
import ua.com.userdb.dto.ReportFilter;
import ua.com.userdb.dto.ReportRowDto;
import ua.com.userdb.dto.ReportSourceType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportsServiceImpl implements ReportsService {

    private final ReportsRepository reportsRepository;

    public ReportsServiceImpl(ReportsRepository reportsRepository) {
        this.reportsRepository = reportsRepository;
    }

    @Override
    public List<ReportRowDto> getReport(ReportFilter filter) {

        List<ReportRowDto> result = new ArrayList<>();

        boolean hasDate = filter.getExpirationTo() != null;

        if (filter.getCertificateTypeId() != null ||
            (filter.getDatabaseId() == null && filter.getDatabaseRoleId() == null)) {

            var certs = hasDate
                ? reportsRepository.findCertificatesWithDate(
                        filter.getDepartmentId(),
                        filter.getCertificateTypeId(),
                        filter.getExpirationTo())
                : reportsRepository.findCertificatesNoDate(
                        filter.getDepartmentId(),
                        filter.getCertificateTypeId());

            certs.forEach(c ->
                result.add(new ReportRowDto(c, ReportSourceType.CERTIFICATE))
            );
        }

        // ===== ДОСТУП ПО РОЛІ =====
        if (filter.getDatabaseRoleId() != null) {

            var rows = hasDate
                ? reportsRepository.findAccessesByRoleWithDate(
                        filter.getDepartmentId(),
                        filter.getDatabaseRoleId(),
                        filter.getExpirationTo())
                : reportsRepository.findAccessesByRoleNoDate(
                        filter.getDepartmentId(),
                        filter.getDatabaseRoleId());

            rows.forEach(r ->
                result.add(new ReportRowDto(r, ReportSourceType.ACCESS))
            );
        }

        // ===== ДОСТУП ПО БД =====
        else if (filter.getDatabaseId() != null) {

            var rows = hasDate
                ? reportsRepository.findAccessesByDatabaseWithDate(
                        filter.getDepartmentId(),
                        filter.getDatabaseId(),
                        filter.getExpirationTo())
                : reportsRepository.findAccessesByDatabaseNoDate(
                        filter.getDepartmentId(),
                        filter.getDatabaseId());

            rows.forEach(r ->
                result.add(new ReportRowDto(r, ReportSourceType.ACCESS))
            );
        }

        result.sort(Comparator.comparing(
            ReportRowDto::getExpirationDate,
            Comparator.nullsLast(Comparator.naturalOrder())
        ));

        return result;
    }

}

