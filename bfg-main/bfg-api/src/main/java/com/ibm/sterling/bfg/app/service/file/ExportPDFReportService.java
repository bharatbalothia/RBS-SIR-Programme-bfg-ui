package com.ibm.sterling.bfg.app.service.file;

import com.ibm.sterling.bfg.app.model.file.SEPAFile;
import com.ibm.sterling.bfg.app.model.file.Transaction;
import com.ibm.sterling.bfg.app.model.report.Column;
import com.ibm.sterling.bfg.app.model.report.Table;
import com.ibm.sterling.bfg.app.model.report.TableBuilder;
import com.ibm.sterling.bfg.app.service.report.ConverterToPDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExportPDFReportService {
    private static final Logger LOGGER = LogManager.getLogger(ExportPDFReportService.class);

    @Autowired
    private ConverterToPDF converterToPDF;

    private static final String TABLE_NAME_FILE = "SEPA Files";
    private static final String TABLE_NAME_TRANSACTIONS = "Transactions for %s(%d)";
    private static final PDRectangle PAGE_SIZE = PDRectangle.A4;
    private static final float TABLE_MARGIN = 25;
    private static final float TABLE_MARGIN_TRANSACTION = 80;
    private static final boolean IS_LANDSCAPE = true;
    private static final PDFont TEXT_FONT = PDType1Font.HELVETICA;
    private static final PDFont HEADER_TEXT_FONT = PDType1Font.HELVETICA_BOLD;
    private static final float FONT_SIZE = 10;
    private static final float ROW_HEIGHT = 15;
    private static final float CELL_MARGIN = 3;
    private float TABLE_HEIGHT_LANDSCAPE = PAGE_SIZE.getWidth() - (2 * TABLE_MARGIN);
    private float TABLE_HEIGHT = PAGE_SIZE.getHeight() - (2 * TABLE_MARGIN);
    private static final String DATE_FORMAT = "ddMMyy";
    private static final int NAME_LENGTH = 50;

    public ByteArrayInputStream generatePDFReport(List<SEPAFile> files) throws IOException {
        LOGGER.info("Generate a PDF report of files");
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("SI.No", 45));
        columns.add(new Column("File Name", 370));
        columns.add(new Column("Type", 50));
        columns.add(new Column("Transaction", 70));
        columns.add(new Column("Total Settlement Amount", 130));
        columns.add(new Column("Settlement Date", 83));
        columns.add(new Column("Direction", 52));
        String[][] content = new String[files.size()][7];
        int index = 0;
        for(SEPAFile file : files) {
            content[index][0] = String.valueOf(index + 1);
            content[index][1] = cutByMaxLength(file.getFilename());
            content[index][2] = file.getType();
            content[index][3] = String.valueOf(file.getTransactionTotal());
            content[index][4] = String.valueOf(file.getSettleAmountTotal());
            content[index][5] = Optional.ofNullable(file.getTimestamp())
                    .map(date -> date.format(DateTimeFormatter.ofPattern(DATE_FORMAT))).orElse("");
            content[index][6] = file.getDirection();
            index++;
        }
        Table table = new TableBuilder()
                .withName(TABLE_NAME_FILE)
                .withCellMargin(CELL_MARGIN)
                .withColumns(columns)
                .withContent(content)
                .withFontSize(FONT_SIZE)
                .withHeight(TABLE_HEIGHT_LANDSCAPE)
                .withLandscape(IS_LANDSCAPE)
                .withLeftMargin(TABLE_MARGIN)
                .withTopMargin(TABLE_MARGIN)
                .withPageSize(PAGE_SIZE)
                .withRowHeight(ROW_HEIGHT)
                .withRowNumber(content.length)
                .withTextFont(TEXT_FONT)
                .withHeaderTextFont(HEADER_TEXT_FONT)
                .build();

        return converterToPDF.convertTableToPDF(table);
    }

    private String cutByMaxLength(String name) {
        return name.substring(0, Math.min(NAME_LENGTH, name.length()));
    }

    public ByteArrayInputStream generatePDFReportForTransactions(List<Transaction> transactions, String fileName, Integer fileId) throws IOException {
        LOGGER.info("Generate a PDF report of transactions for {}", fileName);
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("SI.No", 60));
        columns.add(new Column("Transaction ID", 370));
        columns.add(new Column("Type", 55));
        columns.add(new Column("Settlement amount", 110));
        columns.add(new Column("Settlement Date", 85));
        String[][] content = new String[transactions.size()][7];
        int index = 0;
        for(Transaction transaction : transactions) {
            content[index][0] = String.valueOf(index + 1);
            content[index][1] = cutByMaxLength(String.valueOf(transaction.getTransactionID()));
            content[index][2] = transaction.getType();
            content[index][3] = String.valueOf(transaction.getSettleAmount());
            content[index][4] = Optional.ofNullable(transaction.getSettleDate())
                    .map(date -> date.format(DateTimeFormatter.ofPattern(DATE_FORMAT))).orElse("");
            index++;
        }
        Table table = new TableBuilder()
                .withName(String.format(TABLE_NAME_TRANSACTIONS, fileName, fileId))
                .withCellMargin(CELL_MARGIN)
                .withColumns(columns)
                .withContent(content)
                .withFontSize(FONT_SIZE)
                .withHeight(TABLE_HEIGHT_LANDSCAPE)
                .withLandscape(IS_LANDSCAPE)
                .withLeftMargin(TABLE_MARGIN_TRANSACTION)
                .withTopMargin(TABLE_MARGIN)
                .withPageSize(PAGE_SIZE)
                .withRowHeight(ROW_HEIGHT)
                .withRowNumber(content.length)
                .withTextFont(TEXT_FONT)
                .withHeaderTextFont(HEADER_TEXT_FONT)
                .build();

        return converterToPDF.convertTableToPDF(table);
    }
}
