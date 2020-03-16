package com.inventory.sbg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import net.proteanit.sql.DbUtils;

public class ProductTablePane extends JDialog {

	private DefaultTableModel model;

	private JTable table;
	private JButton printReportB;
	private JTextField vendorIdField;
	private Calendar calendar;
	private SimpleDateFormat sformat;

	public ProductTablePane() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("LIST OF PRODUCT ON STOCK");
		setSize(800, 580);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setModal(true);
		refreshTable();// all products display by thread

		Image myIcon = Toolkit.getDefaultToolkit()
				.getImage(getClass().getResource("/com/inventory/sbg/resource/images/SBGf.png"));
		setIconImage(myIcon);

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		JLabel titleLabel = new JLabel();
		titleLabel.setText("<html><h3 style = color:rgb(255,0,43)> PRODUCT INFORMATION IN DATABASE<hr /></h3><html>");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		northPanel.add(titleLabel, BorderLayout.NORTH);
		northPanel.setBackground(Color.WHITE);
		add(northPanel, BorderLayout.NORTH);
		//
		// center panel that contains table
		JPanel tablePanel = new JPanel();
		tablePanel.setBackground(Color.GRAY);
		table = new JTable();
		String[] columnName = { "Product Name", "QuantityDeleted", "Update Date", "Delete Date", "Sale Date" };
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(columnName);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(700, 300));
		//
		// TableColumnModel colModel = table.getColumnModel();
		// colModel.getColumn(0).setPreferredWidth(30);
		tablePanel.add(scroll);
		add(tablePanel, BorderLayout.CENTER);
		//
		// panel that host search text field and panel table
		JPanel deleteVendor_panel = new JPanel(new FlowLayout());
		deleteVendor_panel.setBorder(new EmptyBorder(20, 20, 20, 20));

		JButton deleteVendorBtn = new JButton("DELETE");
		deleteVendorBtn.setPreferredSize(new Dimension(150, 30));
		deleteVendorBtn.addActionListener(new VendorDelete());
		deleteVendorBtn.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		vendorIdField = new JTextField();
		vendorIdField.setPreferredSize(new Dimension(100, 30));

		//
		// refresh the table after purchase has been made
		printReportB = new JButton("PRINT");
		printReportB.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		printReportB.setPreferredSize(new Dimension(100, 30));

		printReportB.addActionListener(new ProductListPrintingListener());
		

		JLabel idLabel = new JLabel("Enter Vendor Name or ID to Delete:");
		idLabel.setPreferredSize(new Dimension(230, 30));
		idLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		deleteVendor_panel.add(idLabel);
		deleteVendor_panel.add(vendorIdField);
		deleteVendor_panel.add(deleteVendorBtn);
		deleteVendor_panel.add(printReportB);
		add(deleteVendor_panel, BorderLayout.SOUTH);
	}

	// populating combo box and table in update panel
	public void allProduct() {
		PreparedStatement ps = null;

		ResultSet rs = null;

		String qry = "Select * from ProductCategories";

		try {
			ps = DbConnection.getConnection().prepareStatement(qry);

			rs = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));

		} catch (SQLException exc) {
			exc.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				DbConnection.getConnection().close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private class VendorDelete implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String vendorId = vendorIdField.getText().trim().toString().toUpperCase();

			String deleteQry = "Delete from ProductCategories where ID =?";
			int choice = JOptionPane.showConfirmDialog(null, "Do you Want to Delete Vendor With Id " + vendorId);
			if (choice == JOptionPane.YES_OPTION) {
				PreparedStatement ps = null;

				try {
					ps = DbConnection.getConnection().prepareStatement(deleteQry);
					ps.setString(1, vendorId);

					ps.execute();
					allProduct();// refresh the table of products
					vendorIdField.setText("");

				} catch (SQLException exc) {
					exc.printStackTrace();
				} finally {
					try {
						ps.close();
						DbConnection.getConnection().close();
					} catch (SQLException exc) {
						exc.printStackTrace();
					}
				}
			} else {
				return;
			}

			//redresh product table after deleting
			refreshTable();
		}

	}

	private class ProductListPrintingListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Document doc = new Document();
			calendar = Calendar.getInstance();
			sformat = new SimpleDateFormat("yyyy-MM-dd");
			try {

				PdfWriter.getInstance(doc, new FileOutputStream("C:\\BusinessTransaction\\SBG_Customers\\"
						+ "Report\\ProductList\\" + " "
						+ sformat.format(calendar.getTime()) + "SBGProductList.pdf"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			doc.open();
			try {

				Paragraph datePara = new Paragraph(sformat.format(calendar.getTime()));
				datePara.setAlignment(Element.ALIGN_RIGHT);
				doc.add(datePara);

				// URL logoUrl = ItemClass1.class.getResource("./sbg image/");
				com.itextpdf.text.Image myImage = com.itextpdf.text.Image
						.getInstance(getClass().getResource("/com/inventory/sbg/resource/images/display2.png"));

				float[] column = { 0.4f, 1.5f };
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
				Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.UNDERLINE, BaseColor.RED);
				Paragraph redParag = new Paragraph("LIST OF PRODUCTS", redFont);
				redParag.setAlignment(Element.ALIGN_CENTER);
				redParag.setSpacingAfter(30);
				doc.add(redParag);

				// column width
				float[] columnWidth = { 1.2f, 2.4f, 7f, 2f, 2.2f, 2.2f };
				// pdf table
				PdfPTable pdfTable = new PdfPTable(columnWidth);
				// table width percentage of the page width
				pdfTable.setWidthPercentage(90f);
				pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfTable.addCell("S/No");
				pdfTable.addCell("Vendor");
				pdfTable.addCell("Product Descriptions");
				pdfTable.addCell("Quantity");
				pdfTable.addCell("Unit Price");
				pdfTable.addCell("Selling Price");
				pdfTable.setHeaderRows(1);
				pdfTable.setSpacingBefore(20);
				PdfPCell[] cells = pdfTable.getRow(0).getCells();
				for (int i = 0; i < cells.length; i++) {
					cells[i].setBackgroundColor(BaseColor.ORANGE);
				}
				// select dat from data base

				try {
					PreparedStatement ps = null;
					ResultSet rs = null;

					String sql = "Select ID, Vendor, ProductName, Quantity, UnitPrice, "
							+ "SellingPrice From ProductCategories";
					ps = DbConnection.getConnection().prepareStatement(sql);

					rs = ps.executeQuery();
					while (rs.next()) {

						String id = rs.getString("ID");
						String vendorName = rs.getString("Vendor");
						String productName = rs.getString("ProductName");
						String quantity = rs.getString("Quantity");
						String unitPrice = rs.getString("UnitPrice");
						String sellingPric = rs.getString("SellingPrice");
						//

						pdfTable.addCell(id);
						pdfTable.addCell(vendorName);
						pdfTable.addCell(productName);
						pdfTable.addCell(quantity);
						pdfTable.addCell(unitPrice);
						pdfTable.addCell(sellingPric);

					}
					Paragraph tableP = new Paragraph();
					tableP.setAlignment(Element.ALIGN_CENTER);
					tableP.add(pdfTable);
					doc.add(tableP);
					openPdfFolder();//open the save product lists
					doc.close();
				} catch (SQLException excp) {
					excp.printStackTrace();
				}

			} catch (DocumentException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private void openPdfFolder() {
		if (Desktop.isDesktopSupported()) {

			File myFile = new File("C:\\BusinessTransaction\\SBG_Customers\\"
					+ "Report\\ProductList\\" + " "
					+ sformat.format(calendar.getTime()) + "SBGProductList.pdf");
			try {
				Desktop.getDesktop().open(myFile);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Adobe reader is not installed On "
						+ "the System\n Install and try aagain");
			}
		}

	}
	
	private void refreshTable() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(1000);
					for(int i=0;i<1;i++){
						allProduct();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}).start();
		
	}
}
