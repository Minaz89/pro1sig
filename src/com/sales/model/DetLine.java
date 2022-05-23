/**
 *
 * @author by Mina M. Eshak for Project Sales Invoice
 */
package com.sales.model;

// The Details Class which will hold the Details of invoice
public class DetLine {

    private String item;
    private double price;
    private int count;
    private Invoice invoice;

    public DetLine() {
    }

    public DetLine(String item, double price, int count) {

        this.item = item;
        this.price = price;
        this.count = count;
    }

    public DetLine(String item, double price, int count, Invoice invoice) {

        this.item = item;
        this.price = price;
        this.count = count;
        this.invoice = invoice;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public double getDetLineTotal() {
        return price * count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "DetLine{" + "no=" + invoice.getNo() + ", item=" + item + ", price=" + price + ", count=" + count + '}';
    }

    public String getAsCSV() {
        return invoice.getNo() + "," + item + "," + price + "," + count;

    }

}
