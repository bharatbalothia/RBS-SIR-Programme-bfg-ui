package com.ibm.sterling.bfg.app.service.file;

import com.ibm.sterling.bfg.app.model.file.SEPAFile;
import com.ibm.sterling.bfg.app.service.PropertyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportReportService {

    private static final Logger LOGGER = LogManager.getLogger(ExportReportService.class);

    public ByteArrayInputStream generateExcelReport(List<SEPAFile> files) throws IOException {
        LOGGER.info("Generate an excel report");
        String[] columns = {"SI.No", "File Name", "Type", "Transaction", "Total\n settlement\n Amount", "Settlement\n Date", "Direction"};
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("SEPA Files");
            sheet.createFreezePane(0, 1);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.VIOLET.index);

            XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            setBorderStyle(headerStyle, BorderStyle.MEDIUM);
            headerStyle.setWrapText(true);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

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

            int rowIndex = 1;
            for (SEPAFile file : files) {
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
}
