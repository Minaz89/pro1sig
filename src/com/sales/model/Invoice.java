/**
 *
 * @author by Mina M. Eshak for Project Sales Invoice
 */
package com.sales.model;

import java.util.ArrayList;

//The Master Invoice Class which will hold the summary of invoices
public class Invoice {

    private int no;
    private String Date;
    private String Cust;
    private ArrayList<DetLine> lines;

    public Invoice() {
    }

    public Invoice(int no, String Date, String Cust) {
        this.no = no;
        this.Date = Date;
        this.Cust = Cust;
    }

    public double getInvoiceTotal() {
        double total = 0.0;
        for (DetLine detline : getLines()) {
            total += detline.getDetLineTotal();
        }
        return total;
    }

    public String getCust() {
        return Cust;
    }

    public void setCust(String Cust) {
        this.Cust = Cust;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public ArrayList<DetLine> getLines() {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines;
    }

    @Override
    public String toString() {
        return "Invoice{" + "no=" + no + ", Date=" + Date + ", Cust=" + Cust + '}';
    }

    public String getAsCSV() {
        return no + "," + Date + "," + Cust;

    }

}
