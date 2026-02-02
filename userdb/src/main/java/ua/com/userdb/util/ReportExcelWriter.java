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

    private static CellStyle createHeaderStyle(Workbook workbook, IndexedColors color) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle createCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    public static void writeCertificates(List<ReportRowDto> rows, OutputStream os) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Certificates");

            Row header = sheet.createRow(0);
            String[] columns = {"ІПН", "ПІБ", "Підрозділ", "Активний", "Тип сертифікату", "Дата закінчення"};

            CellStyle headerStyle = createHeaderStyle(workbook, IndexedColors.LIGHT_GREEN);
            CellStyle cellStyle = createCellStyle(workbook);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.cloneStyleFrom(cellStyle);
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

            int rowIdx = 1;
            for (ReportRowDto r : rows) {
                Row row = sheet.createRow(rowIdx++);
                int col = 0;

                Cell cell1 = row.createCell(col++);
                cell1.setCellValue(r.getIdentificationNumber() != null ? String.valueOf(r.getIdentificationNumber()) : "");
                cell1.setCellStyle(cellStyle);

                Cell cell2 = row.createCell(col++);
                cell2.setCellValue(r.getUserName());
                cell2.setCellStyle(cellStyle);

                Cell cell3 = row.createCell(col++);
                cell3.setCellValue(r.getDepartmentName());
                cell3.setCellStyle(cellStyle);

                Cell cell4 = row.createCell(col++);
                cell4.setCellValue(r.getIsActive() != null && r.getIsActive() ? "Так" : "Ні");
                cell4.setCellStyle(cellStyle);

                Cell cell5 = row.createCell(col++);
                cell5.setCellValue(r.getCertificateTypeName() != null ? r.getCertificateTypeName() : "");
                cell5.setCellStyle(cellStyle);

                Cell cell6 = row.createCell(col++);
                if (r.getExpirationDate() != null) {
                    cell6.setCellValue(DATE_FORMAT.format(r.getExpirationDate()));
                }
                cell6.setCellStyle(dateStyle);
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 2000);
            }

            workbook.write(os);
        }
    }

    public static void writeAccesses(List<ReportRowDto> rows, OutputStream os) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Accesses");

            Row header = sheet.createRow(0);
            String[] columns = {"ІПН", "ПІБ", "Підрозділ", "Активний", "База даних", "Роль", "Дата закінчення"};

            CellStyle headerStyle = createHeaderStyle(workbook, IndexedColors.LIGHT_CORNFLOWER_BLUE);
            CellStyle cellStyle = createCellStyle(workbook);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.cloneStyleFrom(cellStyle);
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

            int rowIdx = 1;
            for (ReportRowDto r : rows) {
                Row row = sheet.createRow(rowIdx++);
                int col = 0;

                Cell cell1 = row.createCell(col++);
                cell1.setCellValue(r.getIdentificationNumber() != null ? String.valueOf(r.getIdentificationNumber()) : "");
                cell1.setCellStyle(cellStyle);

                Cell cell2 = row.createCell(col++);
                cell2.setCellValue(r.getUserName());
                cell2.setCellStyle(cellStyle);

                Cell cell3 = row.createCell(col++);
                cell3.setCellValue(r.getDepartmentName());
                cell3.setCellStyle(cellStyle);

                Cell cell4 = row.createCell(col++);
                cell4.setCellValue(r.getIsActive() != null && r.getIsActive() ? "Так" : "Ні");
                cell4.setCellStyle(cellStyle);

                Cell cell5 = row.createCell(col++);
                cell5.setCellValue(r.getDatabaseName() != null ? r.getDatabaseName() : "");
                cell5.setCellStyle(cellStyle);

                Cell cell6 = row.createCell(col++);
                cell6.setCellValue(r.getDatabaseRoleName() != null ? r.getDatabaseRoleName() : "");
                cell6.setCellStyle(cellStyle);

                Cell cell7 = row.createCell(col++);
                if (r.getExpirationDate() != null) {
                    cell7.setCellValue(DATE_FORMAT.format(r.getExpirationDate()));
                }
                cell7.setCellStyle(dateStyle);
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 2000);
            }

            workbook.write(os);
        }
    }
}
