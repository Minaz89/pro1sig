/**
 *
 * @author by Mina M. Eshak for Project Sales Invoice
 */
package com.sales.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class DetTableModel extends AbstractTableModel {

    private ArrayList<DetLine> detLines;

    private String[] colums = {"No.", "Item Name", "Item Price", "Count", "Item Total"};

    public DetTableModel(ArrayList<DetLine> detLines) {
        this.detLines = detLines;
    }

    public ArrayList<DetLine> getDetLines() {
        return detLines;
    }

    @Override
    public int getRowCount() {
        return detLines.size();
    }

    @Override
    public int getColumnCount() {
        return colums.length;
    }

    @Override
    public String getColumnName(int column) {
        return colums[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DetLine detLine = detLines.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return detLine.getInvoice().getNo();
            case 1:
                return detLine.getItem();
            case 2:
                return detLine.getPrice();
            case 3:
                return detLine.getCount();
            case 4:
                return detLine.getDetLineTotal();
            default:
                return "";
        }
    }

}
