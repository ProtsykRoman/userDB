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

    /**
     * Універсальний метод для створення Excel звіту
     * 
     * @param rows - дані звіту
     * @param os - OutputStream для Excel
     * @param includeAccessColumns - чи включати колонки "База даних" та "Роль"
     * @param includeCertificateColumns - чи включати колонки "Тип сертифікату"
     * @throws IOException
     */
    public static void writeReport(List<ReportRowDto> rows, OutputStream os,
                                   boolean includeAccessColumns, boolean includeCertificateColumns) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Report");

            // ==== Підготовка заголовків ====
            int baseColumns = 4; // ІПН, ПІБ, Підрозділ, Активний
            int extraColumns = 0;
            if (includeAccessColumns) extraColumns += 2; // База, Роль
            if (includeCertificateColumns) extraColumns += 1; // Тип сертифікату
            extraColumns += 1; // Дата завжди остання

            String[] columns = new String[baseColumns + extraColumns];
            int col = 0;
            columns[col++] = "ІПН";
            columns[col++] = "ПІБ";
            columns[col++] = "Підрозділ";
            columns[col++] = "Активний";

            if (includeAccessColumns) {
                columns[col++] = "База даних";
                columns[col++] = "Роль";
            }

            if (includeCertificateColumns) {
                columns[col++] = "Тип сертифікату";
            }

            columns[col++] = "Дата закінчення";

            writeRows(workbook, sheet, columns, rows, includeAccessColumns, includeCertificateColumns);

            workbook.write(os);
        }
    }

    private static void writeRows(Workbook workbook, Sheet sheet, String[] columns, List<ReportRowDto> rows,
                                  boolean includeAccess, boolean includeCertificate) {

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle cellStyle = createCellStyle(workbook);
        CellStyle dateStyle = createDateCellStyle(workbook, cellStyle);

        // ===== HEADER =====
        Row header = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // ===== DATA =====
        for (int rIdx = 0; rIdx < rows.size(); rIdx++) {
            ReportRowDto r = rows.get(rIdx);
            Row row = sheet.createRow(rIdx + 1);
            int col = 0;

            Cell cell = row.createCell(col++);
            cell.setCellValue(r.getIdentificationNumber() != null ? String.valueOf(r.getIdentificationNumber()) : "");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(col++);
            cell.setCellValue(r.getUserName() != null ? r.getUserName() : "");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(col++);
            cell.setCellValue(r.getDepartmentName() != null ? r.getDepartmentName() : "");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(col++);
            cell.setCellValue(r.getIsActive() != null && r.getIsActive() ? "Так" : "Ні");
            cell.setCellStyle(cellStyle);

            if (includeAccess) {
                cell = row.createCell(col++);
                cell.setCellValue(r.getDatabaseName() != null ? r.getDatabaseName() : "");
                cell.setCellStyle(cellStyle);

                cell = row.createCell(col++);
                cell.setCellValue(r.getDatabaseRoleName() != null ? r.getDatabaseRoleName() : "");
                cell.setCellStyle(cellStyle);
            }

            if (includeCertificate) {
                cell = row.createCell(col++);
                cell.setCellValue(r.getCertificateTypeName() != null ? r.getCertificateTypeName() : "");
                cell.setCellStyle(cellStyle);
            }

            cell = row.createCell(col++);
            if (r.getExpirationDate() != null) {
                cell.setCellValue(DATE_FORMAT.format(r.getExpirationDate()));
            }
            cell.setCellStyle(dateStyle);

            // Додаємо стиль для пустих клітинок
            for (int i = 0; i < col; i++) {
                if (row.getCell(i) == null) {
                    Cell empty = row.createCell(i);
                    empty.setCellStyle(cellStyle);
                }
            }
        }

        // ==== AUTO SIZE ====
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 2000);
        }
    }
}
