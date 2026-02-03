package ua.com.userdb.util;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ua.com.userdb.dto.ReportRowDto;

public class ReportExcelWriter {

    private ReportExcelWriter() { }

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setAllBorders(style);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle createCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setAllBorders(style);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private static CellStyle createDateCellStyle(Workbook workbook, CellStyle baseStyle) {
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.cloneStyleFrom(baseStyle);
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
        return dateStyle;
    }

    private static void setAllBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    // ====================== CERTIFICATES ======================
    public static void writeCertificates(List<ReportRowDto> rows, OutputStream os) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Certificates");
            String[] columns = { "ІПН", "ПІБ", "Підрозділ", "Активний", "Тип сертифікату", "Дата закінчення" };
            writeRows(workbook, sheet, columns, rows, false, true, true);
            workbook.write(os);
        }
    }

    // ====================== ACCESSES ======================
    public static void writeAccesses(List<ReportRowDto> rows, OutputStream os) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Accesses");
            String[] columns = { "ІПН", "ПІБ", "Підрозділ", "Активний", "База даних", "Роль", "Дата закінчення" };
            writeRows(workbook, sheet, columns, rows, true, false, false);
            workbook.write(os);
        }
    }

    // ====================== ACCESSES & CERTIFICATES ======================
    public static void writeAccessesAndCertificates(List<ReportRowDto> rows, OutputStream os,
                                                    boolean showCertificateColumn) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Accesses & Certificates");

            String[] columns;
            if (showCertificateColumn) {
                columns = new String[] { "ІПН", "ПІБ", "Підрозділ", "Активний", "База даних", "Роль", "Тип сертифікату", "Дата закінчення" };
            } else {
                columns = new String[] { "ІПН", "ПІБ", "Підрозділ", "Активний", "База даних", "Роль", "Дата закінчення" };
            }

            writeRows(workbook, sheet, columns, rows, true, showCertificateColumn, showCertificateColumn);
            workbook.write(os);
        }
    }

    // ====================== COMMON WRITE LOGIC ======================
    private static void writeRows(Workbook workbook, Sheet sheet, String[] columns, List<ReportRowDto> rows,
                                  boolean includeDatabase, boolean includeCertificate, boolean showCertificateColumn) {

        Row header = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle cellStyle = createCellStyle(workbook);
        CellStyle dateStyle = createDateCellStyle(workbook, cellStyle);

        // ===== HEADER =====
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // ===== DATA =====
        for (int rowIdx = 0; rowIdx < rows.size(); rowIdx++) {
            ReportRowDto r = rows.get(rowIdx);
            Row row = sheet.createRow(rowIdx + 1);
            int col = 0;

            Cell cell1 = row.createCell(col++);
            cell1.setCellValue(r.getIdentificationNumber() != null ? String.valueOf(r.getIdentificationNumber()) : "");
            cell1.setCellStyle(cellStyle);

            Cell cell2 = row.createCell(col++);
            cell2.setCellValue(r.getUserName() != null ? r.getUserName() : "");
            cell2.setCellStyle(cellStyle);

            Cell cell3 = row.createCell(col++);
            cell3.setCellValue(r.getDepartmentName() != null ? r.getDepartmentName() : "");
            cell3.setCellStyle(cellStyle);

            Cell cell4 = row.createCell(col++);
            cell4.setCellValue(r.getIsActive() != null && r.getIsActive() ? "Так" : "Ні");
            cell4.setCellStyle(cellStyle);

            if (includeDatabase) {
                Cell cell5 = row.createCell(col++);
                cell5.setCellValue(r.getDatabaseName() != null ? r.getDatabaseName() : "");
                cell5.setCellStyle(cellStyle);

                Cell cell6 = row.createCell(col++);
                cell6.setCellValue(r.getDatabaseRoleName() != null ? r.getDatabaseRoleName() : "");
                cell6.setCellStyle(cellStyle);
            }

            if (includeCertificate && showCertificateColumn) {
                Cell cell7 = row.createCell(col++);
                cell7.setCellValue(r.getCertificateTypeName() != null ? r.getCertificateTypeName() : "");
                cell7.setCellStyle(cellStyle);
            }

            // Дата завжди остання
            Cell dateCell = row.createCell(col++);
            if (r.getExpirationDate() != null) {
                dateCell.setCellValue(DATE_FORMAT.format(r.getExpirationDate()));
            }
            dateCell.setCellStyle(dateStyle);

            // Додаємо рамку для всіх клітинок на рядку навіть пустих
            for (int i = 0; i < col; i++) {
                if (row.getCell(i) == null) {
                    Cell emptyCell = row.createCell(i);
                    emptyCell.setCellStyle(cellStyle);
                }
            }
        }

        // ===== AUTO SIZE =====
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 2000);
        }
    }
}
