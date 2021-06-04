package com.ibm.sterling.bfg.app.service.report;

import com.ibm.sterling.bfg.app.model.report.Column;
import com.ibm.sterling.bfg.app.model.report.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ConverterToPDF {
    private static final Logger LOGGER = LogManager.getLogger(ConverterToPDF.class);

    public ByteArrayInputStream convertTableToPDF(Table table) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PDDocument document = new PDDocument();
        try {
            drawTableInPDF(document, table);
            document.save(out);
            document.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void drawTableInPDF(PDDocument document, Table table) throws IOException {
        Integer rowsPerPage = new Double(Math.floor(table.getHeight() / table.getRowHeight())).intValue() - 1;
        Integer pageCount = new Double(Math.ceil(table.getRowNumber() / rowsPerPage)).intValue() + 1;

        for (int pageNumber = 0; pageNumber < pageCount; pageNumber ++) {
            PDPage page = new PDPage();
            page.setMediaBox(table.getPageSize());
            page.setRotation(table.getRotation());
            document.addPage(page);
            PDPageContentStream contentStream = generateContentStreamPerPage(document, page, table);
            String[][] currentPageContent = getContentPerPage(table, rowsPerPage, pageNumber);
            drawTableSubsetToPage(table, contentStream, currentPageContent, pageNumber, pageCount);
        }
    }

    private void drawTableSubsetToPage(
            Table table, PDPageContentStream contentStream, String[][] currentPageContent, int pageNumber, int pageCount) throws IOException {
        float tableTopY = table.isLandscape() ? table.getPageSize().getWidth() - table.getTopMargin() :
                table.getPageSize().getHeight() - table.getTopMargin();
        drawTableGrid(table, currentPageContent, contentStream, tableTopY);

        if (pageNumber == 0) {
            float nameLength = (float) table.getName().length();
            float tableNameTopY = table.isLandscape() ?
                    table.getPageSize().getWidth() - table.getTopMargin() / 3 * 2 :
                    table.getPageSize().getHeight() - table.getTopMargin() / 3 * 2;
            float tableNameTopX = table.isLandscape() ?
                    table.getPageSize().getHeight() / 2 - table.getLeftMargin() - nameLength / 2 :
                    table.getLeftMargin() + nameLength / 2;
            writeTableName(contentStream, tableNameTopX, tableNameTopY, table);
        }

        float nextTextX = table.getLeftMargin() + table.getCellMargin();
        float nextTextY = tableTopY - (table.getRowHeight() / 2)
                - ((table.getTextFont().getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * table.getFontSize()) / 4);

        writeTableHeaders(table.getColumns(), contentStream, nextTextX, nextTextY, table);
        nextTextY -= table.getRowHeight();
        nextTextX = table.getLeftMargin() + table.getCellMargin();

        for (int i = 0; i < currentPageContent.length; i++) {
            writeRowContent(currentPageContent[i], contentStream, nextTextX, nextTextY, table);
            nextTextY -= table.getRowHeight();
            nextTextX = table.getLeftMargin() + table.getCellMargin();
        }
        writePageNumber(pageNumber, pageCount, table, contentStream);
        contentStream.close();
    }

    private void writeTableName(PDPageContentStream contentStream, float nextTextX,
                                float nextTextY, Table table) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(nextTextX, nextTextY);
        contentStream.showText(table.getName());
        contentStream.endText();
    }

    private void writeTableHeaders(List<Column> columns, PDPageContentStream contentStream, float nextTextX,
                                   float nextTextY, Table table) throws IOException {
        for(Column column : columns) {
            contentStream.beginText();
            contentStream.newLineAtOffset(nextTextX, nextTextY);
            contentStream.showText(Optional.ofNullable(column.getName()).orElse(""));
            contentStream.endText();
            nextTextX += column.getWidth();
        }
    }

    private void writeRowContent(String[] lineContent, PDPageContentStream contentStream, float nextTextX, float nextTextY,
                                 Table table) throws IOException {
        contentStream.setFont(table.getTextFont(), table.getFontSize());
        for (int i = 0; i < table.getColumnsCount(); i++) {
            String text = lineContent[i];
            contentStream.beginText();
            contentStream.newLineAtOffset(nextTextX, nextTextY);
            contentStream.showText(Optional.ofNullable(text).orElse(""));
            contentStream.endText();
            nextTextX += table.getColumns().get(i).getWidth();
        }
    }

    private void writePageNumber(int pageNumber, int pageCount, Table table, PDPageContentStream contentStream)
            throws IOException {
        contentStream.setFont(PDType1Font.COURIER, 8);
        contentStream.beginText();
        contentStream.newLineAtOffset(table.getLeftMargin(), table.getTopMargin() / 2);
        contentStream.showText("Page " + (pageNumber + 1) + " from " + pageCount);
        contentStream.endText();
    }

    private void drawTableGrid(Table table, String[][] currentPageContent,
                               PDPageContentStream contentStream, float tableTopY) throws IOException {
        float nextY = tableTopY;
        for (int i = 0; i <= currentPageContent.length + 1; i++) {
            contentStream.moveTo(table.getLeftMargin(), nextY);
            contentStream.lineTo(table.getLeftMargin() + table.getWidth(), nextY);
            contentStream.stroke();
            nextY -= table.getRowHeight();
        }

        final float tableYLength = table.getRowHeight() + (table.getRowHeight() * currentPageContent.length);
        final float tableBottomY = tableTopY - tableYLength;
        float nextX = table.getLeftMargin();
        for (int i = 0; i < table.getColumnsCount(); i++) {
            contentStream.moveTo(nextX, tableTopY);
            contentStream.lineTo(nextX, tableBottomY);
            contentStream.stroke();
            nextX += table.getColumns().get(i).getWidth();
        }
        contentStream.moveTo(nextX, tableTopY);
        contentStream.lineTo(nextX, tableBottomY);
        contentStream.stroke();
    }

    private String[][] getContentPerPage(Table table, int rowsPerPage, int pageNumber) {
        int fromIndex = rowsPerPage * pageNumber;
        int toIndex = rowsPerPage * (pageNumber + 1);
        if (toIndex > table.getRowNumber()) {
            toIndex = table.getRowNumber();
        }
        return Arrays.copyOfRange(table.getContent(), fromIndex, toIndex);
    }

    private PDPageContentStream generateContentStreamPerPage(PDDocument document, PDPage page, Table table)
            throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(table.getHeaderTextFont(), table.getFontSize());
        if (table.isLandscape()) {
            contentStream.transform(new Matrix(0, 1, -1, 0, table.getPageSize().getWidth(), 0));
        }
        return contentStream;
    }
}
