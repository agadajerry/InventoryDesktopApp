package receipt.print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import com.toedter.calendar.JDateChooser;

public class MySalePanel extends JDialog {
	// instance variable
	private static final long serialVersionUID = 1L;
	private JLabel itemLabelQ, itemLabelUP, itemLabelTP;
	private JTextField itemFieldQ, itemFieldUP, itemFieldTP = new JTextField();
	private JButton clearB, deleteB, listB;
	public JTable table;
	public DefaultTableModel model;
	double qtty, qAndP, uPrice, grandTotal;
	public JTextField idField;
	public static JTextField customerField, customerAddrField;
	private JDateChooser chooser;
	public JComboBox<String> productBox, vendorBox;
	private DefaultComboBoxModel<String> vendorComboModel = new DefaultComboBoxModel<String>();
	private DefaultComboBoxModel<String> productComboModel = new DefaultComboBoxModel<String>();


//constructor
	public MySalePanel() {

		setLayout(new BorderLayout());

		setLayout(new BorderLayout());
		// Image icon = Toolkit.getDefaultToolkit().getImage(
		// "C:\\Users\\ENGR_IDOKO\\eclipse-workspace\\" +
		// "DataBases\\src\\receipt\\print\\sbg image\\sale.JPEG");
		// setIconImage(icon);
		initUI();
		setSize(1200, 720);
		setTitle("Purchase And Receipt Issuance");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);// dont exit dialog till operation finished

		// method for populating vendor combo box from database with this method
		productAndVendorList();

		// this method select last row from the customer database
		customerLastRow();

	}

	private void initUI() {
		JPanel northPanel = new JPanel();
		// this is a label for the point of sale
		JLabel pos = new JLabel();
		pos.setText("<html><h3 style = color:rgb(0,0,0)> POINT OF SALES AND TRANSACTION MONITORING<hr /></h3><html>");
		//

		northPanel.add(pos);

		northPanel.setBackground(new Color(240, 98, 146));
		// *******************************************************************************************************

		JPanel westPanel = new JPanel(new BorderLayout());

		JPanel westPanelHolder = new JPanel(new GridLayout(10, 2, 10, 10));

		westPanelHolder.setBackground(new Color(220, 237, 200));
		JLabel dateLabel = new JLabel("DATE OF TRANSACTION:");//

		dateLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		chooser = new JDateChooser();
		chooser.setLocale(Locale.US);
		chooser.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		customerField = new JTextField();
		JLabel cusLabel = new JLabel("CUSTOMER'S NAME:");
		cusLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		JLabel cusAddr = new JLabel("CUSTOMER'S ADDR:");
		cusAddr.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		customerAddrField = new JTextField();
		JLabel idLabel = new JLabel("CUSTOMER ID:");
		idLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		idField = new JTextField();
		JLabel vendorBoxLabel = new JLabel("NAME OF VENDOR:");
		vendorBoxLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		vendorBox = new JComboBox<String>(vendorComboModel);
		vendorBox.setEditable(true);
		AutoCompleteDecorator.decorate(vendorBox);
		vendorBox.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		productBox = new JComboBox<String>(productComboModel);
		productBox.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		productBox.setEditable(true);

		JLabel productName = new JLabel();
		productName.setText("PRODUCT NAME:");
		productName.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		productName.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		AutoCompleteDecorator.decorate(productBox);

		itemLabelQ = new JLabel("QUANTITY:");
		itemLabelQ.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		itemLabelUP = new JLabel("SELLING PRICE:");
		itemLabelUP.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		itemLabelTP = new JLabel("TOTAL PRICE:");
		itemLabelTP.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		//
		itemFieldQ = new JTextField();
		itemFieldQ.addFocusListener(new FocusForSellingPrice());
		itemFieldUP = new JTextField();
		itemFieldTP = new JTextField();
		itemFieldTP.addFocusListener(new TotalPriceListener());
		// ADD the components to west Panel holder
		westPanelHolder.add(dateLabel);
		westPanelHolder.add(chooser);
		//
		westPanelHolder.add(cusLabel);
		westPanelHolder.add(customerField);
		//
		westPanelHolder.add(cusAddr);
		westPanelHolder.add(customerAddrField);
		//
		westPanelHolder.add(idLabel);
		westPanelHolder.add(idField);
		//
		westPanelHolder.add(vendorBoxLabel);
		westPanelHolder.add(vendorBox);
		//
		westPanelHolder.add(productName);
		westPanelHolder.add(productBox);
		westPanelHolder.setBorder(new EmptyBorder(30, 30, 30, 30));

		//
		// center panel inside westPanel
		JPanel southPanelInWest = new JPanel(new GridLayout(4, 2, 10, 10));
		southPanelInWest.add(itemLabelQ);
		southPanelInWest.add(itemFieldQ);
		//
		southPanelInWest.add(itemLabelUP);
		southPanelInWest.add(itemFieldUP);
		//
		southPanelInWest.add(itemLabelTP);
		southPanelInWest.add(itemFieldTP);
		//
		JButton buttonAdd = new JButton();
		buttonAdd.setText("ADD TO THE TABLE");
		buttonAdd.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		buttonAdd.setPreferredSize(new Dimension(20, 30));

		buttonAdd.addActionListener(new ItemAddListener());
		southPanelInWest.add(new JLabel(""));
		southPanelInWest.add(buttonAdd);
		southPanelInWest.setBorder(new EmptyBorder(30, 30, 30, 30));
		southPanelInWest.setBackground(new Color(240, 98, 146));
		//
		// adding the centerPanelInWest, and westPanelHolder to westPanel
		westPanel.add(westPanelHolder, BorderLayout.CENTER);
		westPanel.add(southPanelInWest, BorderLayout.SOUTH);
		add(westPanel, BorderLayout.WEST);
		add(northPanel, BorderLayout.NORTH);
		// *****************************************************************************************

		// table to the east of the layout
		JPanel eastPanel = new JPanel(new BorderLayout());
		JPanel southPanelInEast = new JPanel(new GridLayout(1, 0, 20, 10));
		southPanelInEast.setBackground(new Color(240, 98, 146));
		JPanel eastPanelCenter = new JPanel();
		// a table that display what has been entered
		table = new JTable();
		table.setBorder(BorderFactory.createLineBorder(Color.RED));
		JScrollPane scrollable = new JScrollPane(table);
		scrollable.setPreferredSize(new Dimension(500, 450));
		String ColNames[] = { "Name Of Items", "Quantity", "Unit Price", "Total Price" };
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(ColNames);

		// resize table column header
		TableColumnModel colModel = table.getColumnModel();
		colModel.getColumn(0).setPreferredWidth(250);
		//
		// clear and view list button
		listB = new JButton("SAVE TO PDF AND PRINT");
		listB.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		listB.setToolTipText("The files are saved To drive C://Transactions...");
		listB.addActionListener(new ItemLists());
		clearB = new JButton("CLEAR TABLE");
		clearB.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		clearB.setPreferredSize(new Dimension(20, 30));

		clearB.addActionListener(new ClearTable());
		deleteB = new JButton("DELETE ROW");
		deleteB.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

		deleteB.addActionListener(new RowTableListener());

		// add the table to the east_panel_
		eastPanelCenter.add(scrollable);
		eastPanelCenter.setBorder(new EmptyBorder(30, 30, 30, 30));
		eastPanelCenter.setBackground(new Color(220, 237, 200));
		southPanelInEast.add(listB);
		southPanelInEast.add(clearB);
		southPanelInEast.add(deleteB);
		southPanelInEast.setBorder(new EmptyBorder(30, 30, 30, 30));

		//
		eastPanel.add(eastPanelCenter, BorderLayout.CENTER);
		eastPanel.add(southPanelInEast, BorderLayout.SOUTH);
		add(eastPanel, BorderLayout.EAST);
		// date of the business transaction
		Calendar carl = Calendar.getInstance();
		SimpleDateFormat bizDate = new SimpleDateFormat("dd/MM/yyyy");
		JLabel timeLabel = new JLabel();

		timeLabel.setText(bizDate.format(carl.getTime()) + "|  ");
		timeLabel.setBackground(new Color(220, 237, 200));
		northPanel.add(timeLabel);
		// center Panel from separation
		JPanel centerPanelColor = new JPanel();
		centerPanelColor.setBackground(new Color(240, 98, 146));
		ImageIcon image = new ImageIcon("./src/receipt/print/sbg image/loginSuccessful.JPG");
		// JLabel imageLabel = new JLabel();

		centerPanelColor.add(new JLabel(image));
		add(centerPanelColor, BorderLayout.CENTER);

	}

	public void customerLastRow() {
		/*
		 * Select last row id from the customer table and adding PreparedStatement ps
		 * =null;
		 */
		PreparedStatement ps = null;
		ResultSet rSet = null;
		String lastRow = "Select CustId from CustomerList order by CustId Desc limit 1";
		try {
			ps = ConnectionProductCat.getConnection().prepareStatement(lastRow);
			rSet = ps.executeQuery();
			while (rSet.next()) {
				int lastId = Integer.parseInt(rSet.getString("CustId"));
				idField.setText(idField.getText() + (lastId + 1));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				ConnectionProductCat.getConnection().close();
				ps.close();
				rSet.close();
			} catch (SQLException exce) {
				exce.printStackTrace();
			}
		}
	}// end of customer Last row method
		//

	public void productAndVendorList() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement pre = null;
		ResultSet res = null;

		String vendorQuery = "Select DISTINCT Vendor from ProductCategories order by Vendor";
		String productQuery = "Select ProductName from ProductCategories order by ProductName desc";
		try {

			ps = ConnectionProductCat.getConnection().prepareStatement(vendorQuery);
			rs = ps.executeQuery();
			pre = ConnectionProductCat.getConnection().prepareStatement(productQuery);
			rs = ps.executeQuery();
			res = pre.executeQuery();
			// resultSet for vendor names
			while (rs.next()) {
				String vendorName = rs.getString("Vendor");
				vendorComboModel.addElement(vendorName);
			}
			// resultSet for product list
			while (res.next()) {
				String productName = res.getString("ProductName");
				productComboModel.addElement(productName);
			}
		} catch (SQLException exc) {
			JOptionPane.showMessageDialog(null, "error has occurred" + exc.getMessage());
		} finally {
			try {
				ConnectionProductCat.getConnection().close();
				ps.close();
				rs.close();
			} catch (SQLException exce) {
				exce.printStackTrace();
			}
		}

	}

	// listener for adding item to the table
	private class ItemAddListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// disable customer name field and id soon

			if (chooser.getDate() == null

					|| itemFieldUP.getText().toString().trim().equals("")
					|| itemFieldTP.getText().toString().trim().equals("")
					|| itemFieldQ.getText().toString().trim().equals("")) {
				JOptionPane.showMessageDialog(null, "ERROR:\n Empty Fields...  ");
			} else {

				model.addRow(
						new String[] { (String) productBox.getSelectedItem(), itemFieldQ.getText().toString().trim(),
								itemFieldUP.getText().toString().trim(), itemFieldTP.getText().toString().trim()

						});

			}
		}

	}

	// clear table class
	private class ClearTable implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			model.setRowCount(0);

		}

	}

	// row delete class
	private class RowTableListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int row = table.getSelectedRow();
			if (row < 0) {
				JOptionPane.showMessageDialog(null,
						" TABLE ROW IS NOT YET SELECTED\n SELECT ROW BEFORE CLICKING THE BUTTON");
			} else
				model.removeRow(row);

		}

	}

	// item for printing class. this class add values from
	private class ItemLists implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// int choice = JOptionPane.showConfirmDialog(null, "Do you Want to print The
			// Selected Items?");
			// if(choice== JOptionPane.YES_OPTION)

			if (customerField.getText().toString().trim().equals("") || idField.getText().toString().trim().equals("")
					|| customerAddrField.getText().toString().trim().equals("") || table.getRowCount() < 0) {

				JOptionPane.showMessageDialog(null, "error:\n Field(s) is empty!");

			} else {
				customerDetails();
				idField.setText("");
				customerLastRow();

				// customerTable();
				ItemClass items = new ItemClass(table, model, customerField, customerAddrField);

				// customerTable();
				items.setVisible(true);

			}
		}
	}

	// customer details to be inserted into it own table
	public void customerDetails() {
		String custName = customerField.getText().toString();
		String cusAddr = customerAddrField.getText().toString();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");

		String bus_date = dateFormat.format(chooser.getDate());
		String cusId = idField.getText().toString();
		PreparedStatement pre = null;
		// result set for last insert row id

		// *************************************************************************************************
		// insertion query
		String sql = "insert into CustomerList(CustId, cusName, CusAddress, Date ) Values (?,?,?,?);";

		try {

			// ps = ConnectionProductCat.getConnection().prepareStatement(tableName);
			// ps.execute();
			pre = ConnectionProductCat.getConnection().prepareStatement(sql);

			pre.setString(1, cusId);

			pre.setString(2, custName);

			pre.setString(3, cusAddr);
			pre.setString(4, bus_date);
			pre.execute();

			// System.out.println("Table created");
		} catch (SQLException exc) {
			JOptionPane.showMessageDialog(null, "Customer List Error" + exc.getMessage());
			// exc.printStackTrace();
		} finally {
			try {
				ConnectionProductCat.getConnection().close();
				pre.close();

			} catch (SQLException ex) {
				// ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "date2" + ex.getMessage());

			}
		}
	}

// focus listener for total_price_field
	private class TotalPriceListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {

			if (itemFieldQ.getText().isEmpty() || itemFieldUP.getText().isEmpty()) {
				itemFieldTP.setText("Error:empty Field(s)");
			} else {
				int quantityBought = Integer.parseInt(itemFieldQ.getText().toString().trim());
				double unitPrice_item = Double.parseDouble(itemFieldUP.getText().toString().trim());

				double total_price = (quantityBought * unitPrice_item);
				itemFieldTP.setText("" + total_price);
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub

		}

	}

	// inner class for selling price auto pick on a particular product
	private class FocusForSellingPrice implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub

			itemFieldUP.setText(null);
			PreparedStatement ps = null;
			String qry = "Select SellingPrice From ProductCategories Where ProductName =?";
			String productName = (String) productBox.getSelectedItem();
			System.out.println("" + productName);
			try {
				ps = ConnectionProductCat.getConnection().prepareStatement(qry);
				ps.setString(1, productName);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					String result = rs.getString("SellingPrice");
					itemFieldUP.setText(itemFieldUP.getText() + result);
				}
			} catch (SQLException exc) {
				exc.printStackTrace();
			} finally {
				try {
					ps.close();
					// ConnectionProductCat.getConnection().close();
				} catch (SQLException exce) {
					exce.printStackTrace();
				}
			}

		}

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
