/**
 *
 * @author by Mina M. Eshak for Project Sales Invoice
 */
package com.sales.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class InvoiceMasterTableModel extends AbstractTableModel {

    private ArrayList<Invoice> invoices;
    private String[] colums = {"No.", "Date", "Cust", "Total"};

    public InvoiceMasterTableModel(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }

    @Override
    public int getRowCount() {
        return invoices.size();
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
        Invoice invoice = invoices.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return invoice.getNo();
            case 1:
                return invoice.getDate();
            case 2:
                return invoice.getCust();
            case 3:
                return invoice.getInvoiceTotal();
            default:
                return "";
        }
    }

}
