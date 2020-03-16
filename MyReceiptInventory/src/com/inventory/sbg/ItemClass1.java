package com.inventory.sbg;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ItemClass1  extends JDialog{
	private static final long serialVersionUID = 1L;
	private JButton pdfBtn;
	private JTextArea display;
	private String title;
	JLabel dirLabel;
	private double grandTotal;

	public ItemClass1(JTable table1, DefaultTableModel model1,  JTextField customerAddrField, JTextField customerField) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Receipt");
		setSize(692, 640);
		setLocationRelativeTo(null);
		setModal(true);
		
		java.awt.Image myIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource
				("/com/inventory/sbg/resource/images/SBGf.png"));
		setIconImage(myIcon);
		
		setLayout(null);
		
		

		// saveItemBought();
		model1 = (DefaultTableModel) table1.getModel();
		display = new JTextArea();
		display.setTabSize(5);
		display.setEnabled(false);

		// summing total price column of the table
		grandTotal = 0.00;
		for (int i = 0; i < table1.getRowCount(); i++) {

			double totalP = Double.parseDouble(table1.getValueAt(i, 3) + "");

			grandTotal = grandTotal + totalP;
		}
		// display.setLineWrap(true);
		// display.setWrapStyleWord(true);
		MySalePanel sPanel = new MySalePanel();
		// System.out.println(sPanel.getText());
		// display.setFont(new Font("Times new Roman", 1, 12));
		title = ("\n\tMotto: His Grace Is \n\tSufficient For Us. 2Cor 12:9\t\t" + ">>INVOICE<<\t\t\t" + "Invoice No:"
				+ sPanel.idField.getText() + "\n\n" + "\n\t\t\t\t   SBG INTEGRATED  SYSTEMS"
				+ "\t\t  RC 2346422\n\t\t\t\t  --------------------------------------------\n\n" + ""
				+ "\tHEAD OFFICE\t\tBRANCH OFFICE 1\t\tBRANCH OFFICE 2\n\t"
				+ "No.32 Apapa/Oshodi\tNo.8 ADP Adada Plaza\t\tNo.99/64 Adada Plaza"
				+ "\n\tExpressway Lagos.\t\tUniversity Market Rd\t\tUniversity Market Rd"
				+ "\n\t\t\t\tNsukka\t\t\t\tNsukka." + "\n\tPhone:07039010942\t"
				+ "Phone 08038308520, 08148056893\tPhone:07039010942\n"

				+ "\t=========================================" + "======================================\n");

		display.append(title + "\t" + model1.getColumnName(0) + "\t\t\t\t" + model1.getColumnName(1) + "\t" + "\t"
				+ model1.getColumnName(2) + "\t" + model1.getColumnName(3) + "\n"
				+ "\t-----------------------------------------------------"
				+ "--------------------------------------------------" + "----------------------------------\t\n\n");

		// loop for jTable rows
		for (int a = 0; a < table1.getRowCount(); a++) {

			display.append("\t" + table1.getModel().getValueAt(a, 0) + "\t\t\t" + table1.getModel().getValueAt(a, 1)
					+ "\t\t" + table1.getModel().getValueAt(a, 2) + "\t\t" + table1.getModel().getValueAt(a, 3) + "\n");

		}
		// Grand total of the selected items
		NumberFormat numberfmt = NumberFormat.getInstance();

		display.append("\t\t\t\t\t\t\t\t=========================\n" + "\t\t\t\t\t\t\t\t" + "Grand Total\t" + "N"
				+ numberfmt.format(grandTotal) + "\n" + "\t\t\t\t\t\t\t\t=========================");

		// **************************************************************
		display.append("\n\n\n\n\n\n\n\n\t" + "Dated\t" + "\n\n");

		//
		// this code is for customer names and and signature

		// MySalePanel sPanel = new MySalePanel();

		// String cusName = sPanel.customerField.getText();
		// String address = addrAndName[1].getText();
		display.append("\tCustomer's Name\t" + customerField.getText() + "\t" + "Sign:--------\t\t"
				+ "Authorised By  -------------" + "\n\n");

		// this code is for address of the customer

		// String address = sPanel.customerAddrField.getText();
		// *******************************************************************************

		display.append("\tAddress\t" + customerAddrField.getText() + "\n\n");

		// terms and conditions
		display.append("\t<<TERMS AND CONDITIONS>>:\n\n\tOnce Goods are sold, it will not be taken back\n"
				+ "\t18% p.a will be charged if the payment is not made within the stipulated time");

		// *************************************************************************

		JScrollPane scroll = new JScrollPane(display);
		scroll.setBounds(10, 10, 653, 540);
		add(scroll);
		
		dirLabel = new JLabel("Receipt is in C\\transaction Folder");
		dirLabel.setBounds(140, 560, 300, 20);
		add(dirLabel);
		pdfBtn = new JButton("Save To PDF And Print");
		pdfBtn.setBounds(450, 560, 150, 30);
		pdfBtn.setBorder(BorderFactory.createMatteBorder(3, 6, 2, 7, Color.BLACK));
		add(pdfBtn);
		pdfBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				customerTrigger();// creation of trigger table in db
				Document doc = new Document();
				try {

					PdfWriter.getInstance(doc, new FileOutputStream("C:\\BusinessTransaction\\SBG_Customers\\" + ""
							+ customerField.getText() + sPanel.idField.getText() + "Receipt.pdf"));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				doc.open();
				try {
					
					//
					Font originalFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLDITALIC, BaseColor.BLACK);

					Paragraph originalText = new Paragraph("ORIGINAL", originalFont);
					doc.add(originalText);

					Font red = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.UNDERLINE, BaseColor.RED);
					Paragraph invoiceParagraph = new Paragraph("INVOICE", red);
					invoiceParagraph.setAlignment(Element.ALIGN_RIGHT);
					doc.add(invoiceParagraph);

					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd");
					Paragraph datePara = new Paragraph(sformat.format(calendar.getTime()));
					datePara.setAlignment(Element.ALIGN_RIGHT);
					doc.add(datePara);
					//
					Paragraph cusId = new Paragraph("ID: " + sPanel.idField.getText().toString());
					cusId.setAlignment(Element.ALIGN_RIGHT);
					doc.add(cusId);

					// company logo
					// image of the company
					//URL logoUrl = ItemClass1.class.getResource("./sbg image/");
					Image myImage = Image.getInstance(getClass().getResource
							("/com/inventory/sbg/resource/images/SBGLog.png"));

					float[] column = { 0.2f, 1.5f };
					// logo table
					PdfPTable table1 = new PdfPTable(column);
					// table width percentage of the page width
					table1.setTotalWidth(1);

					PdfPCell cell1 = new PdfPCell();

					cell1.setBorder(Rectangle.NO_BORDER);

					//
					table1.addCell(myImage);
					// cell1.addElement(myImage);
					cell1.setPhrase(new Phrase("Motto: His Grace Is \r\n" + "	Sufficient For Us. 2Cor 12:9"));
					table1.addCell(cell1);

					Paragraph tableLogo = new Paragraph();
					tableLogo.setAlignment(Element.ALIGN_CENTER);
					tableLogo.add(table1);
					doc.add(tableLogo);
					table1.setSpacingBefore(20);
					table1.setSpacingAfter(50);

					// table for address of the company
					Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.UNDERLINE, BaseColor.BLUE);
					Paragraph redParag = new Paragraph("SBG INTEGRATED SYSTEMS", redFont);
					redParag.setAlignment(Element.ALIGN_CENTER);
					redParag.setSpacingAfter(30);
					doc.add(redParag);

					float[] column1 = { 1.7f, 2.5f, 1.7f };
					// pdf table
					PdfPTable table2 = new PdfPTable(column1);
					// table width percentage of the page width
					table2.setTotalWidth(1);
					table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

					PdfPCell cell = new PdfPCell();

					cell.setBorder(Rectangle.NO_BORDER);

					cell.setPhrase(new Phrase(
							"HEAD OFFICE\n" + "No.32 Apapa Oshodi\n" + "Expressway \nLagos State.\nPhone:07039010942"));
					table2.addCell(cell);
					//
					cell.setPhrase(new Phrase("HEAD OFFICE 1\n" + "No.8 Adp Adada Plaza \n"
							+ "University Market Rd\nNsukka" + "\nPhone:Phone 08038308520, 08148056893"));
					table2.addCell(cell);
					cell.setPhrase(new Phrase("HEAD OFFICE 2\n" + "No.99/64 Adada Plaza\n"
							+ "University Market Rd.\nNsukka" + "\nPhone:07039010942"));
					table2.addCell(cell);

					Paragraph tableP2 = new Paragraph();
					tableP2.setAlignment(Element.ALIGN_CENTER);
					tableP2.add(table2);
					doc.add(tableP2);
					table2.setSpacingBefore(20);
					table2.setSpacingAfter(50);

				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// column width
				float[] columnWidth = { 1f,7f, 1.5f, 2f, 2.2f };
				// pdf table
				PdfPTable pdfTable = new PdfPTable(columnWidth);
				// table width percentage of the page width
				pdfTable.setWidthPercentage(90f);
				pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfTable.addCell("S/No");
				pdfTable.addCell("Product Descriptions");
				pdfTable.addCell("Quantity");
				pdfTable.addCell("Unit Price");
				pdfTable.addCell("Total Price");
				pdfTable.setHeaderRows(1);
				pdfTable.setSpacingBefore(20);
				PdfPCell[] cells = pdfTable.getRow(0).getCells();
				for (int i = 0; i < cells.length; i++) {
					cells[i].setBackgroundColor(BaseColor.ORANGE);
				}

				try {
					for (int i = 0; i < table1.getRowCount(); i++) {
						String productName = (String) table1.getValueAt(i, 0);
						int quantit = Integer.parseInt(table1.getValueAt(i, 1) + "");
						double unitPric = Double.parseDouble(table1.getValueAt(i, 2) + "");
						double totalPric = Double.parseDouble(table1.getValueAt(i, 3) + "");
						
						pdfTable.addCell("" +(1 + i));
						pdfTable.addCell("" + productName);
						pdfTable.addCell("" + quantit);
						pdfTable.addCell("" + unitPric);
						pdfTable.addCell("" + totalPric);
					}
					Paragraph tableP = new Paragraph();
					tableP.setAlignment(Element.ALIGN_CENTER);
					tableP.add(pdfTable);
					doc.add(tableP);
					//
					// another table tht hold total
					float[] column = { 2f, 1.5f };
					// pdf table
					PdfPTable table2 = new PdfPTable(column);
					// table width percentage of the page width
					table2.setWidthPercentage(90f);
					table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					table2.addCell("Grand Total");

					// summing total price column of the table
					grandTotal = 0.00;
					for (int i = 0; i < table1.getRowCount(); i++) {

						double totalP = Double.parseDouble(table1.getValueAt(i, 3) + "");

						grandTotal = grandTotal + totalP;
					}
					
					// number format for currency
					NumberFormat numberfmt = NumberFormat.getInstance();

					table2.addCell("" + numberfmt.format(grandTotal) + " (Naira)");
					table2.setSpacingBefore(10);
					table2.setSpacingAfter(20);

					Paragraph tableP2 = new Paragraph();
					tableP2.setAlignment(Element.ALIGN_CENTER);
					tableP2.add(table2);
					doc.add(tableP2);
					//

					String cusName = customerField.getText().toString();
					String cusAddress = customerAddrField.getText().toString();
					
					 //
		            float[] column3= {  1.5f };
		            // logo table
		            PdfPTable table1 = new PdfPTable(column3);
		            // table width percentage of the page width
		            table1.setTotalWidth(1);

		            PdfPCell cell1 = new PdfPCell();

		            cell1.setBorder(Rectangle.NO_BORDER);


		            cell1.setPhrase(new Phrase("Name: " + cusName));
		            table1.addCell(cell1);
		            //
		            cell1.setPhrase(new Phrase("Address: " + cusAddress));
		            table1.addCell(cell1);

		            Paragraph tableLogo = new Paragraph();
		            tableLogo.setAlignment(Element.ALIGN_CENTER);
		            tableLogo.add(table1);
		            doc.add(tableLogo);
		            table1.setSpacingBefore(20);
		            table1.setSpacingAfter(30);
		            //
					
					Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.UNDERLINE, BaseColor.RED);
					Paragraph redParag = new Paragraph("TERMS AND CONDITIONS", redFont);
					redParag.setAlignment(Element.ALIGN_LEFT);
					redParag.setSpacingAfter(20);
					doc.add(redParag);
					Paragraph termsParagraph = new Paragraph("Once Goods are sold, it will not be taken back, "
							+ "	18% p.a will be charged if the payment is not made within the stipulated time.");
					termsParagraph.setSpacingAfter(20);
					doc.add(termsParagraph);
					Paragraph adminName = new Paragraph("Authorized By:----------------- Signed ----------");
					 adminName.setSpacingBefore(10);
			            adminName.setSpacingAfter(10);
			            adminName.setAlignment(Element.ALIGN_CENTER);
			            doc.add(adminName);
					dirLabel.setText("check : C:\\BusinessTransaction\\SBG_Customers");
					 //QRCODE

		            BarcodeQRCode barCode = new BarcodeQRCode("SBG -- RC234622",1000,1000,null);

		            Image codeQRImage =barCode.getImage();
		            codeQRImage.scaleAbsolute(100,100);
		            doc.add(codeQRImage);
		            
		            

					doc.close();
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (Desktop.isDesktopSupported()) {

					File myFile = new File("C:\\BusinessTransaction\\SBG_Customers\\" + "" + customerField.getText()
							+ sPanel.idField.getText() + "Receipt.pdf");
					try {
						Desktop.getDesktop().open(myFile);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				// ************* When PdfButton is pressed, item Bought will be
				// saved****************
				try {
					PreparedStatement ps, pre = null;

					String querry = "insert into ItemPurchased ( ProductName, Quantity, UnitPrice, TotalPrice) Values(?,?,?,?);";
					String updateTable = "Update ProductCategories set Quantity= Quantity-? where ProductName=?";
					// MySalePanel mySale = new MySalePanel();
					// String id = mySale.idField.getText();

					for (int i = 0; i < table1.getRowCount(); i++) {
						String productName = (String) table1.getValueAt(i, 0);
						int quantit = Integer.parseInt(table1.getValueAt(i, 1) + "");
						double unitPric = Double.parseDouble(table1.getValueAt(i, 2) + "");
						
						ps = DbConnection.getConnection().prepareStatement(querry);
						pre = DbConnection.getConnection().prepareStatement(updateTable);

						ps.setString(1, productName);
						ps.setInt(2, quantit);
						ps.setDouble(3, unitPric);
						ps.setDouble(4, (quantit*unitPric));
						

						pre.setInt(1, quantit);
						pre.setString(2, productName);

						ps.execute();
						pre.executeUpdate();

					}

				} catch (SQLException exc) {
					exc.printStackTrace();

				}
				// ************* When print button is pressed, item Bought will be//
				// saved****************end

			}

		});
	}

	// ***********************************************************************************************

	/*
	 * protected void displayPrint() { PrinterJob job = PrinterJob.getPrinterJob();
	 * if (job.printDialog()) { try { job.setPrintable(new Printable() {
	 * 
	 * @Override public int print(Graphics g, PageFormat pf, int pageIndex) { if
	 * (pageIndex > 0) { return NO_SUCH_PAGE; } Graphics2D g2d = (Graphics2D) g;
	 * g2d.translate(pf.getImageableX(), pf.getImageableY()); display.print(g);
	 * return PAGE_EXISTS;
	 * 
	 * }
	 * 
	 * }); job.print(); } catch (PrinterException pe) { pe.printStackTrace(); } } }
	 */

	private void customerTrigger() {

		String customerTrigerTable = "create Trigger if Not Exists My_CustomerLog AFTER INSERT " + "ON ItemPurchased"
				+ " BEGIN Insert into CustomerLog(Product_Name, Quantity_Bought, SellingPrice, TotalAmount, Date) "
				+ "Values(new.ProductName, new.Quantity, new.UnitPrice, new.TotalPrice, date('now')); " + " END;";
		try {
			PreparedStatement ps = null;
			ps = DbConnection.getConnection().prepareStatement(customerTrigerTable);
			ps.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}


}
