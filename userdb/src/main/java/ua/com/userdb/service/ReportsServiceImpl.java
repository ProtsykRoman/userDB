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

        // ================= CERTIFICATES =================
        if (filter.getCertificateTypeId() != null) {

            var rows = hasDate
                ? reportsRepository.findCertificatesWithDate(
                        filter.getDepartmentId(),
                        filter.getCertificateTypeId(),
                        filter.getExpirationTo())
                : reportsRepository.findCertificatesNoDate(
                        filter.getDepartmentId(),
                        filter.getCertificateTypeId());

            rows.forEach(r ->
                result.add(new ReportRowDto(r, ReportSourceType.CERTIFICATE))
            );

            return sort(result);
        }

        // ================= ACCESS BY ROLE =================
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

            return sort(result);
        }

        // ================= ACCESS BY DATABASE =================
        if (filter.getDatabaseId() != null) {

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

            return sort(result);
        }

        // ================= DEFAULT: CERTIFICATES =================
        var rows = hasDate
            ? reportsRepository.findCertificatesWithDate(
                    filter.getDepartmentId(),
                    null,
                    filter.getExpirationTo())
            : reportsRepository.findCertificatesNoDate(
                    filter.getDepartmentId(),
                    null);

        rows.forEach(r ->
            result.add(new ReportRowDto(r, ReportSourceType.CERTIFICATE))
        );

        return sort(result);
    }

    private List<ReportRowDto> sort(List<ReportRowDto> list) {
        list.sort(Comparator.comparing(
            ReportRowDto::getExpirationDate,
            Comparator.nullsLast(Comparator.naturalOrder())
        ));
        return list;
    }
}


