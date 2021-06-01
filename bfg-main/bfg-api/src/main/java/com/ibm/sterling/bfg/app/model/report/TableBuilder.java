package com.ibm.sterling.bfg.app.model.report;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.util.List;

public class TableBuilder {

    private Table table = new Table();

    public TableBuilder withName(String name) {
        table.setName(name);
        return this;
    }

    public TableBuilder withMargin(float margin) {
        table.setMargin(margin);
        return this;
    }

    public TableBuilder withHeight(float height) {
        table.setHeight(height);
        return this;
    }

    public TableBuilder withPageSize(PDRectangle pageSize) {
        table.setPageSize(pageSize);
        return this;
    }

    public TableBuilder withLandscape(boolean isLandscape) {
        table.setLandscape(isLandscape);
        return this;
    }

    public TableBuilder withRowHeight(float rowHeight) {
        table.setRowHeight(rowHeight);
        return this;
    }

    public TableBuilder withTextFont(PDFont textFont) {
        table.setTextFont(textFont);
        return this;
    }

    public TableBuilder withHeaderTextFont(PDFont textFont) {
        table.setHeaderTextFont(textFont);
        return this;
    }

    public TableBuilder withFontSize(float fontSize) {
        table.setFontSize(fontSize);
        return this;
    }

    public TableBuilder withRowNumber(Integer rowNumber) {
        table.setRowNumber(rowNumber);
        return this;
    }

    public TableBuilder withColumns(List<Column> columns) {
        table.setColumns(columns);
        return this;
    }

    public TableBuilder withContent(String[][] content) {
        table.setContent(content);
        return this;
    }

    public TableBuilder withCellMargin(float cellMargin) {
        table.setCellMargin(cellMargin);
        return this;
    }

    public Table build() {
        return table;
    }
}
