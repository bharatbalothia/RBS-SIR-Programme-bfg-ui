package com.ibm.sterling.bfg.app.service.file;

import com.ibm.sterling.bfg.app.model.file.SEPAFile;
import com.ibm.sterling.bfg.app.model.file.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class ExportExcelReportService {

    private static final Logger LOGGER = LogManager.getLogger(ExportExcelReportService.class);

    public ByteArrayInputStream generateExcelReport(List<SEPAFile> files) throws IOException {
        LOGGER.info("Generate an excel report");
        String[] columns = {"SI.No", "File Name", "Type", "Transaction", "Total\n Settlement\n Amount", "Settlement\n Date", "Direction"};
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
            for (int columnIndex = 0; columnIndex < columns.length; columnIndex++) {
                Cell cell = headerRow.createCell(columnIndex);
                cell.setCellValue(columns[columnIndex]);
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

            for (int i = 0; i < columns.length; i++) {
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

    public ByteArrayInputStream generateExcelReportForTransactions(List<Transaction> transactions, String fileName) throws IOException {
        LOGGER.info("Generate an excel report");
        String[] columns = {"SI.No", "Transaction ID", "Type", "Settlement\n Amount", "Settlement\n Date"};
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Transactions for " + fileName);
            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(new CellRangeAddress(0, 0, 1, 4));

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
            for (int columnIndex = 0; columnIndex < columns.length; columnIndex++) {
                Cell cell = headerRow.createCell(columnIndex);
                cell.setCellValue(columns[columnIndex]);
                cell.setCellStyle(headerStyle);
            }

            CreationHelper creationHelper = workbook.getCreationHelper();
            XSSFCellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(
                    creationHelper.createDataFormat().getFormat("ddMMyyhhmm"));
            setBorderStyle(dateStyle, BorderStyle.THIN);


            Optional.ofNullable(transactions).ifPresent(transactionList -> {
                int rowIndex = 1;
                for (Transaction transaction : transactionList) {
                    Row row = sheet.createRow(rowIndex);
                    Cell cellIndex = row.createCell(0);
                    cellIndex.setCellValue(rowIndex);
                    cellIndex.setCellStyle(cellStyle);

                    Cell cellFileName = row.createCell(1);
                    cellFileName.setCellValue(transaction.getTransactionID());
                    cellFileName.setCellStyle(cellStyle);

                    Cell cellType = row.createCell(2);
                    cellType.setCellValue(transaction.getType());
                    cellType.setCellStyle(cellStyle);

                    Cell cellAmountTotal = row.createCell(3);
                    cellAmountTotal.setCellValue(transaction.getSettleAmount());
                    cellAmountTotal.setCellStyle(cellStyle);

                    Cell cellTimestamp = row.createCell(4);
                    cellTimestamp.setCellValue(transaction.getTimestamp());
                    cellTimestamp.setCellStyle(dateStyle);

                    rowIndex++;
                }
            });

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
