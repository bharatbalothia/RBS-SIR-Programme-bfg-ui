package com.ibm.sterling.bfg.app.model.pdf;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.util.List;

public class Table {
    private float margin;
    private float height;
    private PDRectangle pageSize;
    private boolean isLandscape;
    private float rowHeight;
    private PDFont textFont;
    private float fontSize;
    private Integer rowNumber;
    private List<Column> columns;
    private String[][] content;
    private float cellMargin;

    public Table() {
    }

    public String[] getColumnsNamesAsArray() {
        String[] columnNames = new String[getColumnsCount()];
        for (int i = 0; i < getColumnsCount() - 1; i++) {
            columnNames[i] = columns.get(i).getName();
        }
        return columnNames;
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return columns.stream().map(Column::getWidth).reduce(0f, Float::sum);
    }

    public PDRectangle getPageSize() {
        return pageSize;
    }

    public void setPageSize(PDRectangle pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isLandscape() {
        return isLandscape;
    }

    public void setLandscape(boolean landscape) {
        isLandscape = landscape;
    }

    public Integer getRotation() {
        return isLandscape ? 90 : 0;
    }

    public float getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(float rowHeight) {
        this.rowHeight = rowHeight;
    }

    public PDFont getTextFont() {
        return textFont;
    }

    public void setTextFont(PDFont textFont) {
        this.textFont = textFont;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getColumnsCount() {
        return this.columns.size();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String[][] getContent() {
        return content;
    }

    public void setContent(String[][] content) {
        this.content = content;
    }

    public float getCellMargin() {
        return cellMargin;
    }

    public void setCellMargin(float cellMargin) {
        this.cellMargin = cellMargin;
    }
}
