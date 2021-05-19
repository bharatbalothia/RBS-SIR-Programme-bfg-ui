package com.ibm.sterling.bfg.app.service.file;

import com.ibm.sterling.bfg.app.model.file.SEPAFile;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ExportReportService {

    private static final Logger LOGGER = LogManager.getLogger(ExportReportService.class);
    private static final String[] COLUMNS = {"SI.No", "File Name", "Type", "Transaction", "Total\n Settlement\n Amount", "Settlement\n Date", "Direction"};

    public ByteArrayInputStream generateExcelReport(List<SEPAFile> files) throws IOException {
        LOGGER.info("Generate an excel report");

        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("SEPA Files");
            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(new CellRangeAddress(0, 0, 1, 6));

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            setBorderStyle(headerStyle, BorderStyle.MEDIUM);
            headerStyle.setWrapText(true);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            setBorderStyle(cellStyle, BorderStyle.THIN);

            Row headerRow = sheet.createRow(0);
            for (int columnIndex = 0; columnIndex < COLUMNS.length; columnIndex++) {
                Cell cell = headerRow.createCell(columnIndex);
                cell.setCellValue(COLUMNS[columnIndex]);
                cell.setCellStyle(headerStyle);
            }

            CreationHelper creationHelper = workbook.getCreationHelper();
            XSSFCellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(
                    creationHelper.createDataFormat().getFormat("ddMMyyhhmm"));
            setBorderStyle(dateStyle, BorderStyle.THIN);


            Optional.ofNullable(files).ifPresent(sepaFiles -> {
                int rowIndex = 1;
                for (SEPAFile file : sepaFiles) {
                    Row row = sheet.createRow(rowIndex);
                    Cell cellIndex = row.createCell(0);
                    cellIndex.setCellValue(rowIndex);
                    cellIndex.setCellStyle(cellStyle);

                    Cell cellFileName = row.createCell(1);
                    cellFileName.setCellValue(file.getFilename());
                    cellFileName.setCellStyle(cellStyle);

                    Cell cellType = row.createCell(2);
                    cellType.setCellValue(file.getType());
                    cellType.setCellStyle(cellStyle);

                    Cell cellTrxTotal = row.createCell(3);
                    cellTrxTotal.setCellValue(file.getTransactionTotal());
                    cellTrxTotal.setCellStyle(cellStyle);

                    Cell cellAmountTotal = row.createCell(4);
                    cellAmountTotal.setCellValue(file.getSettleAmountTotal());
                    cellAmountTotal.setCellStyle(cellStyle);

                    Cell cellTimestamp = row.createCell(5);
                    cellTimestamp.setCellValue(file.getTimestamp());
                    cellTimestamp.setCellStyle(dateStyle);

                    Cell cellDirection = row.createCell(6);
                    cellDirection.setCellValue(file.getDirection());
                    cellDirection.setCellStyle(cellStyle);

                    rowIndex++;
                }
            });

            for (int i = 0; i < COLUMNS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void setBorderStyle(XSSFCellStyle headerStyle, BorderStyle borderStyle) {
        headerStyle.setBorderBottom(borderStyle);
        headerStyle.setBorderTop(borderStyle);
        headerStyle.setBorderLeft(borderStyle);
        headerStyle.setBorderRight(borderStyle);
    }

    public ByteArrayInputStream generatePDFReport(List<SEPAFile> files) throws IOException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
//            String header = "SEPA files";
////            Header headerHandler = new Header(header, header);
////            new Footer

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new int[] {65, 220, 55, 100, 100, 100, 80});

            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell headerCell;

            for (String column : COLUMNS) {
                headerCell= new PdfPCell(new Phrase(column, headerFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(headerCell);
            }

            int rowIndex = 100000;

            for (SEPAFile file : files) {
                addPdfCell(table, String.valueOf(rowIndex));
                addPdfCell(table, file.getFilename());
                addPdfCell(table, file.getType());
                addPdfCell(table, String.valueOf(file.getTransactionTotal()));
                addPdfCell(table, String.valueOf(file.getSettleAmountTotal()));
                addPdfCell(table, String.valueOf(file.getTimestamp()));
                addPdfCell(table, file.getDirection());
                rowIndex ++;
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);
            document.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (DocumentException e) {
            LOGGER.error("Error appears in pdf document");
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addPdfCell(PdfPTable table, String type) {
        PdfPCell cell = new PdfPCell(new Phrase(type));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}
