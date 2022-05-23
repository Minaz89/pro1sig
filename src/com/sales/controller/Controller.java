/**
 *
 * @author by Mina M. Eshak for Project Sales Invoice
 */
package com.sales.controller;

import com.sales.model.DetLine;
import com.sales.model.DetTableModel;
import com.sales.model.Invoice;
import com.sales.model.InvoiceMasterTableModel;
import com.sales.view.Invlayout;
import com.sales.view.InvoiceDetLineDailog;
import com.sales.view.InvoiceMasterDailog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import jdk.nashorn.internal.ir.BreakNode;

public class Controller implements ActionListener, ListSelectionListener {

    private Invlayout frame;
    private InvoiceMasterDailog invoiceMasterDailog;
    private InvoiceDetLineDailog invoiceDetLineDailog;

    public Controller(Invlayout frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        /**
         * This code to test actionLister and actionCommand Don't Forget to
         * comment it xD
         */
        switch (actionCommand) {
            case "loadInvoice":
                loadInvoice();
                break;

            case "saveInvoice":
                saveInvoice();
                break;

            case "createNewInvoice":
                createNewInvoice();
                break;

            case "deleteInvoice":
                deleteInvoice();
                break;

            case "createNewItem":
                createNewItem();
                break;

            case "deleteItem":
                deleteItem();
                break;
            case "createInvoiceCancel":
                createInvoiceCancel();
                break;
            case "createInvoiceOK":
                createInvoiceOK();
                break;
            case "createLineOK":
                createLineOK();
                break;
            case "createLineCancel":
                createLineCancel();
                break;
        }

    }

    ArrayList<Invoice> inovicesArray = new ArrayList<>();

    private void loadInvoice() {
        JFileChooser fc = new JFileChooser();
        String invoiceDate = "";
        try {
            int result = fc.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File invoiceFile = fc.getSelectedFile();
                if (!invoiceFile.getAbsolutePath().endsWith(".csv") && !invoiceFile.getAbsolutePath().endsWith(".CSV")) {
                    System.out.println("Wrong file format");
                    int input = JOptionPane.showConfirmDialog(null, "Wrong File Format", "Error Message",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Path headerPath = Paths.get(invoiceFile.getAbsolutePath());

                List<String> allInvoices = Files.readAllLines(headerPath);

                for (String allInvoice : allInvoices) {
                    String[] invoiceParts = allInvoice.split(",");
                    int invoiceNo = Integer.parseInt(invoiceParts[0]);

                    invoiceDate = invoiceParts[1];

                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Date d = formatter.parse(invoiceDate);
                    int invoiceMonth = Integer.parseInt(invoiceDate.split("-")[1]);
                    if (invoiceMonth > 12 || invoiceMonth < 1) {
                        throw new ParseException(allInvoice, result);
                    }
                    String custName = invoiceParts[2];

                    Invoice invoice = new Invoice(invoiceNo, invoiceDate, custName);
                    inovicesArray.add(invoice);

                }
//               Detail invoice  
                result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File detailFile = fc.getSelectedFile();
                    if (!detailFile.getAbsolutePath().endsWith(".csv") && !detailFile.getAbsolutePath().endsWith(".CSV")) {
                        System.out.println("Wrong file format");
                        int input = JOptionPane.showConfirmDialog(null, "Wrong File Format", "Error Message",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Path detailPath = Paths.get(detailFile.getAbsolutePath());
                    List<String> detailinvoices = Files.readAllLines(detailPath);

                    for (String detailinvoice : detailinvoices) {
                        String[] detailParts = detailinvoice.split(",");
                        int invoiceNo = Integer.parseInt(detailParts[0]);
                        String itemName = detailParts[1];
                        double itemPrice = Double.parseDouble(detailParts[2]);
                        if (itemPrice < 0) {
                            System.out.println("Wrong price value");
                            int input = JOptionPane.showConfirmDialog(null, itemPrice + " Item price can not be less than zero.", "Error Message",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                            return;

                        }
                        int count = Integer.parseInt(detailParts[3]);
                        Invoice inv = null;
                        for (Invoice invoice : inovicesArray) {
                            if (invoice.getNo() == invoiceNo) {
                                inv = invoice;
                                break;

                            }
                        }
                        DetLine detline = new DetLine(itemName, itemPrice, count, inv);
                        inv.getLines().add(detline);
                    }

                }

                frame.setInvoices(inovicesArray);
                InvoiceMasterTableModel invoiceMasterTableModel = new InvoiceMasterTableModel(inovicesArray);
                frame.setInvoiceMasterTableModel(invoiceMasterTableModel);
                frame.getTotalTable().setModel(invoiceMasterTableModel);
                frame.getInvoiceMasterTableModel().fireTableDataChanged();

            }
        } catch (IOException exp) {
            exp.printStackTrace();
        } catch (ParseException ex) {
            System.out.println("Wrong date format");
            int input = JOptionPane.showConfirmDialog(null, invoiceDate + " has wrong date format", "Error Message",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void saveInvoice() {

        ArrayList<Invoice> invoices = frame.getInvoices();
        String headsavfile = "";
        String detsavefile = "";
        for (Invoice invoice : invoices) {
            String invCSV = invoice.getAsCSV();
            headsavfile += invCSV;
            headsavfile += "\n";

            for (DetLine detLine : invoice.getLines()) {
                String detCSV = detLine.getAsCSV();
                detsavefile += detCSV;
                detsavefile += "\n";

            }

        }
        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                hfw.write(headsavfile);
                hfw.flush();
                hfw.close();
                result = fc.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {

                    File detFile = fc.getSelectedFile();
                    FileWriter dfw = new FileWriter(detFile);
                    dfw.write(detsavefile);
                    dfw.flush();
                    dfw.close();

                }
            }
        } catch (Exception ex) {

        }

    }

    private void createNewInvoice() {
        invoiceMasterDailog = new InvoiceMasterDailog(frame);
        invoiceMasterDailog.setVisible(true);

    }

    private void deleteInvoice() {
        int selectedRow = frame.getTotalTable().getSelectedRow();
        int input = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirmation Delete Invoice",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (input == 0 && selectedRow != -1) {

            frame.getInvoices().remove(selectedRow);
            frame.getInvoiceMasterTableModel().fireTableDataChanged();
        }

    }

    private void createNewItem() {
        invoiceDetLineDailog = new InvoiceDetLineDailog(frame);
        invoiceDetLineDailog.setVisible(true);

    }

    private void deleteItem() {
        int selectedInv = frame.getTotalTable().getSelectedRow();
        int selectedRow = frame.getDetailsTable().getSelectedRow();
        int input = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirmation Delete Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (input == 0 && selectedInv != -1 && selectedRow != -1) {
            Invoice invoice = frame.getInvoices().get(selectedInv);
            invoice.getLines().remove(selectedRow);
            DetTableModel detTableModel = new DetTableModel(invoice.getLines());
            frame.getDetailsTable().setModel(detTableModel);
            detTableModel.fireTableDataChanged();
            frame.getInvoiceMasterTableModel().fireTableDataChanged();

        }

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

        int selectedIndex = frame.getTotalTable().getSelectedRow();
        if (selectedIndex != -1) {
            Invoice currentInvoice = frame.getInvoices().get(selectedIndex);
            frame.getInvoiceNoLabel().setText("" + currentInvoice.getNo());
            frame.getInvoiceDateLabel().setText(currentInvoice.getDate());
            frame.getCustNameLabel().setText(currentInvoice.getCust());
            frame.getInvoiceTotalLabel().setText("" + currentInvoice.getInvoiceTotal());
            DetTableModel detTableModel = new DetTableModel(currentInvoice.getLines());
            frame.getDetailsTable().setModel(detTableModel);
            detTableModel.fireTableDataChanged();
//          to be checked auto update  InvoiceTotalLabel
//          frame.getInvoiceTotalLabel().firePropertyChange(InvoiceT, selectedIndex, selectedIndex); to be seen 
        }
    }

    private void createInvoiceOK() {
        String date = invoiceMasterDailog.getInvDateField().getText();
        String cust = invoiceMasterDailog.getCustNameField().getText();
        int no = frame.getNxtInvNo();

        Invoice invoice = new Invoice(no, date, cust);
        frame.getInvoices().add(invoice);
        frame.getInvoiceMasterTableModel().fireTableDataChanged();
        invoiceMasterDailog.setVisible(false);
        invoiceMasterDailog.dispose();
        invoiceMasterDailog = null;

    }

    private void createInvoiceCancel() {
        invoiceMasterDailog.setVisible(false);
        invoiceMasterDailog.dispose();
        invoiceMasterDailog = null;

    }

    private void createLineOK() {
        String item = invoiceDetLineDailog.getItemNameField().getText();
        String countStr = invoiceDetLineDailog.getItemCountField().getText();
        String priceStr = invoiceDetLineDailog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);

        if (price < 0) {
            System.out.println("Wrong price value");
            int input = JOptionPane.showConfirmDialog(null, price + " Item price can not be less than zero.", "Error Message",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;

        }

        int selectedInvoice = frame.getTotalTable().getSelectedRow();
        if (selectedInvoice != -1) {
            Invoice invoice = frame.getInvoices().get(selectedInvoice);
            DetLine detLine = new DetLine(item, price, count, invoice);
            invoice.getLines().add(detLine);
            DetTableModel detTableModel = (DetTableModel) frame.getDetailsTable().getModel();
            detTableModel.fireTableDataChanged();
            frame.getInvoiceMasterTableModel().fireTableDataChanged();
        }
        invoiceDetLineDailog.setVisible(false);
        invoiceDetLineDailog.dispose();
        invoiceDetLineDailog = null;
    }

    private void createLineCancel() {
        invoiceDetLineDailog.setVisible(false);
        invoiceDetLineDailog.dispose();
        invoiceDetLineDailog = null;

    }

}
