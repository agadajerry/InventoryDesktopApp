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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

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

public class HistoryPane extends JDialog {
	private static final long serialVersionUID = 1L;
	private JCheckBox quantity_remainCheck;
	private DefaultTableModel model;
	private Calendar calendar;
	private SimpleDateFormat sformat;
	private JTable table;
	private JComboBox<String> dateField;
	private JButton dateButton, printB;
	private JLabel todaySaleLabel;
	private DefaultComboBoxModel<String> dateModel = new DefaultComboBoxModel<String>();

	private double grandTotal = 0.00;

	public HistoryPane() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("TRANSACTION HISTORY");
		setSize(800, 630);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setModal(true);
		dateComboBox();// date combbox box

		initUI();

		// URL urlImage = HistoryPane.class.getResource("./sbg image/");

		Image icon = Toolkit.getDefaultToolkit()
				.getImage(getClass().getResource("/com/inventory/sbg/resource/images/SBGLog.png"));

		setIconImage(icon);
	}

	private void initUI() {

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		JLabel titleLabel = new JLabel();
		titleLabel.setText("<html><h3 style = color:rgb(255,0,0)> TRANSACTION HISTORY<hr /></h3><html>");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		northPanel.add(titleLabel, BorderLayout.NORTH);
		// check box

		// date chooser
		quantity_remainCheck = new JCheckBox();

		quantity_remainCheck.addItemListener(new ItemRemainingListener());
		quantity_remainCheck.setPreferredSize(new Dimension(100, 20));
		JPanel northEastPanel = new JPanel(new FlowLayout());
		// add date and label to north east panel
		northEastPanel.add(new JLabel("Item On Stock"));
		northEastPanel.add(quantity_remainCheck);

		northPanel.add(northEastPanel, BorderLayout.EAST);

		add(northPanel, BorderLayout.NORTH);
		// center panel that contains table
		JPanel tablePanel = new JPanel();
		table = new JTable();
		String[] columnName = { "Product Name", "", "Update Date", "Delete ", "Sale Date" };
		model = (DefaultTableModel) table.getModel();
		// table.setAutoResizeMode(2);;
		model.setColumnIdentifiers(columnName);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(700, 300));

		//
		// selection of date that item was bought
		dateField = new JComboBox<String>(dateModel);
		dateField.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		dateField.setEditable(true);
		// dateField = new JTextField("");
		dateField.setToolTipText("Enter Transaaction date");

		dateField.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		dateField.setPreferredSize(new Dimension(140, 30));
		tablePanel.add(dateField);
		dateButton = new JButton("Ok");
		dateButton.addActionListener(new BizDateLstener());
		tablePanel.add(dateButton);
		tablePanel.add(scroll);
		// table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(300);

		add(tablePanel, BorderLayout.CENTER);

		// daily sale label info
		todaySaleLabel = new JLabel("Total Amount");
		todaySaleLabel.setForeground(Color.BLACK);
		//
		printB = new JButton("Print Report");
		printB.addActionListener(new PurchaseHistoryPrinting());

		JPanel southPanel = new JPanel();
		southPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		southPanel.add(todaySaleLabel, BorderLayout.CENTER);
		southPanel.add(printB, BorderLayout.EAST);

		add(southPanel, BorderLayout.SOUTH);

		// *****************************************************************************************************

		purchaseHistory();

	}

	private void remainingQuantity() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String countQuantity = "select Sum(TotalAmount) AS totalPaid from CustomerLog where Date = ?;";
		try {
			ps = DbConnection.getConnection().prepareStatement(countQuantity);
			ps.setString(1, (String) dateField.getSelectedItem());
			rs = ps.executeQuery();
			while (rs.next()) {

				int totalPaid = rs.getInt("totalPaid");

				NumberFormat nFormat = NumberFormat.getInstance();

				todaySaleLabel.setText("TOTAL AMOUNT OF ITEMS SOLD ON " + dateField.getSelectedItem() + "   ====== "
						+ nFormat.format(totalPaid) + "  Naira ==== ");
			}

		} catch (SQLException exc) {
			exc.printStackTrace();

		} finally {
			try {
				if (rs != null) {
					rs.close();
					DbConnection.getConnection().close();

				}
			} catch (SQLException exce) {
				exce.printStackTrace();
			}
		}

	}

	private void purchaseHistory() {
		PreparedStatement ps = null;
		ResultSet rSet = null;
		String sql = "Select * From CustomerLog";
		try {
			ps = DbConnection.getConnection().prepareStatement(sql);

			// ps.setString(1, dateField.getText());
			rSet = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rSet));

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				DbConnection.getConnection().close();
			} catch (SQLException exc) {
				exc.printStackTrace();
			}
		}
	}

	// item on stock listener
	private class ItemRemainingListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {

			int state = e.getStateChange();
			if (state == ItemEvent.SELECTED) {
				selectRemainingItems();

			} else if (state == ItemEvent.DESELECTED) {
				purchaseHistory();
			}

		}

	}

	// method that select data from product categories
	private void selectRemainingItems() {
		String sql = "Select Vendor, ProductName, Quantity, UnitPrice, SellingPrice from ProductCategories";
		PreparedStatement ps = null;
		try {
			ps = DbConnection.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null,
					"Error Just occured On Selection \nfrom of remaining product info" + ex.getMessage());
		}
	}

	private class BizDateLstener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			remainingQuantity(); // this method sum daily sale made

			PreparedStatement ps = null;
			ResultSet rSet = null;
			String sql = "Select * From CustomerLog where Date =?";
			try {
				ps = DbConnection.getConnection().prepareStatement(sql);

				ps.setString(1, (String) dateField.getSelectedItem());
				rSet = ps.executeQuery();
				table.setModel(DbUtils.resultSetToTableModel(rSet));

			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				try {
					DbConnection.getConnection().close();
				} catch (SQLException exc) {
					exc.printStackTrace();
				}
			}

		}
	}

	private void dateComboBox() {
		PreparedStatement ps = null;
		ResultSet rSet = null;
		String sql = "Select DISTINCT Date From CustomerLog";
		try {
			ps = DbConnection.getConnection().prepareStatement(sql);

			rSet = ps.executeQuery();
			while (rSet.next()) {
				String transDate = rSet.getString("Date");
				dateModel.addElement(transDate);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private class PurchaseHistoryPrinting implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			historyReport();

		}

		private void historyReport() {
			Document doc = new Document();

			try {

				PdfWriter.getInstance(doc, new FileOutputStream("C:\\BusinessTransaction\\SBG_Customers\\Report\\" + " "
						+ dateField.getSelectedItem() + "Report.pdf"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			doc.open();
			try {

				
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
				Paragraph redParag = new Paragraph("TRANSACTION  REPORT ON "+dateField.getSelectedItem(), redFont);
				redParag.setAlignment(Element.ALIGN_CENTER);
				redParag.setSpacingAfter(30);
				doc.add(redParag);

				// column width
				float[] columnWidth = { 6f, 2.1f, 2.2f, 2.2f };
				// pdf table
				PdfPTable pdfTable = new PdfPTable(columnWidth);
				// table width percentage of the page width
				pdfTable.setWidthPercentage(90f);
				pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				// pdfTable.addCell("S/No");
				pdfTable.addCell("Product Bought");
				pdfTable.addCell("Quantity");
				pdfTable.addCell("Selling Price");
				pdfTable.addCell("TotalAmount");
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

					String sql = "Select  Product_Name, Quantity_Bought, SellingPrice, TotalAmount"
							+ " From CustomerLog where Date =?";

					ps = DbConnection.getConnection().prepareStatement(sql);

					ps.setString(1, (String) dateField.getSelectedItem());
					rs = ps.executeQuery();
					while (rs.next()) {

						String productName = rs.getString("Product_Name");
						String quantit = rs.getString("Quantity_Bought");
						String sellingp = rs.getString("SellingPrice");
						String totalp = rs.getString("TotalAmount");

						grandTotal += Double.parseDouble(totalp);

						// pdfTable.addCell(id);
						pdfTable.addCell(productName);
						pdfTable.addCell(quantit);
						pdfTable.addCell(sellingp);
						pdfTable.addCell(totalp);
						// pdfTable.addCell(sellingPric);

					}
					Paragraph tableP = new Paragraph();
					tableP.setAlignment(Element.ALIGN_CENTER);
					tableP.add(pdfTable);
					doc.add(tableP);

					// another table tht hold total
					float[] column1 = { 2f, 1.5f };
					// pdf table
					PdfPTable table2 = new PdfPTable(column1);
					// table width percentage of the page width
					table2.setWidthPercentage(90f);
					table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					table2.addCell("Grand Total");

					// number format for currency
					NumberFormat numberfmt = NumberFormat.getInstance();

					table2.addCell("" + numberfmt.format(grandTotal) + " (Naira)");
					table2.setSpacingBefore(10);
					table2.setSpacingAfter(20);

					Paragraph tableP2 = new Paragraph();
					tableP2.setAlignment(Element.ALIGN_CENTER);
					tableP2.add(table2);
					doc.add(tableP2);
					openPdfFolder();// open the save product lists
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

			File myFile = new File("C:\\BusinessTransaction\\SBG_Customers\\Report\\" + " "
					+ dateField.getSelectedItem() + "Report.pdf");
			try {
				Desktop.getDesktop().open(myFile);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Adobe reader is not installed On "
						+ "the System\n Install and try aagain");
			}
		}

	}

}
