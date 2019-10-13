package receipt.print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;

public class ProductPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField productField, quantityField, unitField, sellingField, vendorField, vendorIdField;
	private JPanel centerPanel;
	private JButton addB, updateB, refreshB;

	private JTable table;
	private DefaultTableModel model;

	public ProductPanel() {

		//productTable();

		setLayout(new BorderLayout());

		initUI();
		allProduct();

	}

	private void initUI() {

		// north panel that hold title label
		JPanel northContainer = new JPanel(new GridLayout(1, 0, 20, 20));
		JLabel titleLabel = new JLabel();
		titleLabel
				.setText("<html><h3 style = color:rgb(0,0,0)> STOCK KEEPING AND PRODUCT INFORMATION<hr /></h3><html>");

		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		northContainer.add(titleLabel);
		northContainer.setBackground(new Color(240, 98, 146));
		// center_Header is the description of panel that host center_panel

		JPanel centerHeader = new JPanel(new BorderLayout());
		centerHeader.setBackground(new Color(220, 237, 200));

		centerPanel = new JPanel(new GridLayout(10, 2,10,10));
		// centerPanel.setBackground(new Color(220,237,200));
		 //centerPanel.setBorder(new EmptyBorder(10,10,10,10));
		JLabel vendorLabel = new JLabel("VENDOR:");
		vendorLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		JLabel productLabel = new JLabel("PRODUCT NAME:");
		productLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		JLabel quantityLabel = new JLabel("QUANTITY BOUGHT:");
		quantityLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		JLabel unitLabel = new JLabel("UNIT PRICE:");
		unitLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		JLabel sellingLabel = new JLabel("SELLING PRICE:");
		sellingLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		// fields
		vendorField = new JTextField();
		vendorField.setPreferredSize(new Dimension(250, 30));
		productField = new JTextField();
		productField.setPreferredSize(new Dimension(250, 30));
		quantityField = new JTextField();
		quantityField.setPreferredSize(new Dimension(250, 30));
		unitField = new JTextField();
		unitField.setPreferredSize(new Dimension(250, 30));
		sellingField = new JTextField();
		sellingField.setPreferredSize(new Dimension(250, 30));
		// add all label and fields to panel

		
		centerPanel.add(vendorLabel);
		centerPanel.add(vendorField);
		centerPanel.add(productLabel);
		centerPanel.add(productField);
		centerPanel.add(quantityLabel);
		centerPanel.add(quantityField);
		centerPanel.add(unitLabel);
		centerPanel.add(unitField);
		centerPanel.add(sellingLabel);
		centerPanel.add(sellingField);
		

		// adding center panel to the west of the panel(centerPanel)

		centerHeader.add(centerPanel, BorderLayout.CENTER);
		centerHeader.setBorder(new EmptyBorder(40, 40, 40, 40));

		// a panel that contains Buttons which will be added to the center_panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		addB = new JButton("ADD RECORD");
		addB.setPreferredSize(new Dimension(100, 30));
		addB.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		addB.addActionListener(new AddedItemListener());

		updateB = new JButton("UPDATE");
		updateB.setPreferredSize(new Dimension(100, 30));
		updateB.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		updateB.addActionListener(new UpdateListener());
		
		
		buttonPanel.add(addB);
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(updateB);
		buttonPanel.setBackground(new Color(240, 98, 146));
		buttonPanel.setBorder(new EmptyBorder(40, 20, 40, 20));

		// adding the panel to the panel header

		centerHeader.add(buttonPanel, BorderLayout.SOUTH);
		// allPanel.add(westPanel,BorderLayout.EAST);
		// westPanel.setBackground(new Color(240, 98, 146));
		JPanel centerImage = new JPanel();
		
		ImageIcon image = new ImageIcon("./src/receipt/print/sbg image/MyStock.png");

		add(northContainer, BorderLayout.NORTH);
		add(centerImage.add(new JLabel(image)),BorderLayout.CENTER);
		add(centerHeader, BorderLayout.WEST);

		// *******************************************************************************************

		// table of product added to the database
		// this table display newly added vendor and items currently in the database
		String ColumnName[] = { "VendorName", "ProductName", "Quantity", "UnitPrice", "TotalPrice" };

		table = new JTable();
		table.setShowVerticalLines(false);
		// table.setBackground(Color.RED);
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(ColumnName);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(600, 200));

		// *******************************************************************************

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
		//refresh the table after purchase has been made
				refreshB = new JButton("Refresh");
				refreshB.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						//sqlite query method for selecting values from the product table
						allProduct();
						
					}
					
				});

				deleteVendor_panel.add(refreshB);

		JLabel idLabel = new JLabel("Enter Vendor Name or ID to Delete:");
		idLabel.setPreferredSize(new Dimension(230, 30));
		idLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		deleteVendor_panel.add(idLabel);
		deleteVendor_panel.add(vendorIdField);
		deleteVendor_panel.add(deleteVendorBtn);

		// south panel for table of data from the database
		JPanel eastPanel = new JPanel(new BorderLayout());
		eastPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		eastPanel.add(new JLabel("PRODUCT TABLE In THE DATABASE",SwingConstants.CENTER),BorderLayout.NORTH);
		eastPanel.add(scroll,BorderLayout.CENTER);
		eastPanel.setBackground(new Color(220, 237, 200));
		
		//eastPanel.add(checkBox);
		eastPanel.add(deleteVendor_panel, BorderLayout.SOUTH);

		// panel that hold searchPanel and southPanel in one place

		
		// deleteVendor_panel.setBackground(new Color(220,237,200));
		deleteVendor_panel.setBackground(new Color(240, 98, 146));

		add(eastPanel, BorderLayout.EAST);

		

	}

	// populating combo box and table in update panel
	public void allProduct() {
		PreparedStatement ps = null;

		ResultSet rs = null;

		String qry = "Select * from ProductCategories" + " Order by Vendor, ProductName";

		try {
			ps = ConnectionProductCat.getConnection().prepareStatement(qry);

			rs = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));

		} catch (SQLException exc) {
			exc.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				ConnectionProductCat.getConnection().close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	// adding item to the data base --this method addNewItem
	public void addNewItem() {
		String vendorName = vendorField.getText().toString().toUpperCase().trim();
		String productName = productField.getText().toString().toUpperCase().trim();
		String quantityNo = quantityField.getText().toString().toUpperCase().trim();
		String unitNo = unitField.getText().toString().toUpperCase().trim();
		String sellingNo = sellingField.getText().toString().toUpperCase().trim();
		
		// condition that check if text box is empty or not
		if (vendorName.isEmpty() || productName.isEmpty() || quantityNo.isEmpty() || unitNo.isEmpty()
				|| sellingNo.isEmpty()||quantityNo.contains(".")) {
			JOptionPane.showMessageDialog(null, "Empty text Field(s)...\n Check The entered Values!");
		} else {

			PreparedStatement ps = null;
			
			String sql = "insert into ProductCategories (Vendor, ProductName, Quantity, UnitPrice, SellingPrice)"
					+ " Values (?,?,?,?,?);";

			try {

				// ************************************************************************************************
				ps = ConnectionProductCat.getConnection().prepareStatement(sql);

				ps.setString(1, vendorName);
				ps.setString(2, productName);
				ps.setString(3, quantityNo);
				ps.setString(4, unitNo);
				ps.setString(5, sellingNo);
				

				ps.executeUpdate();

				JOptionPane.showMessageDialog(null, "Data Saved Successfully...");
				vendorField.setText("");
				productField.setText("");
				quantityField.setText("");
				unitField.setText("");
				sellingField.setText("");

			} catch (SQLException sqlx) {
				sqlx.printStackTrace();
			} finally {
				try {
					ConnectionProductCat.getConnection().close();
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, "Error just occured" + ex);
				}
			}
		}
	}
	// select item from the product category and display on jlist

	private class AddedItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			addNewItem();
			// populating combo and JList method
			allProduct();

		}

	}

	// update method
	public void upDate()

	{
		String vendorName = vendorField.getText().toString().toUpperCase().trim();
		String productName = productField.getText().toString().toUpperCase().trim();
		String quantityNo = quantityField.getText().toString().toUpperCase().trim();
		String unitNo = unitField.getText().toString().toUpperCase().trim();
		String sellingNo = sellingField.getText().toString().toUpperCase().trim();

		// *******************************************************************************************88
		if (vendorName.isEmpty() || productName.isEmpty() || quantityNo.isEmpty() || unitNo.isEmpty()
				|| sellingNo.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Empty text Fields\n Ensure that all fields are filled..");
		} else {

			PreparedStatement ps = null;
			String sql = "Update ProductCategories SET ProductName=?, Quantity=?, UnitPrice =?, SellingPrice =? where Vendor =?";
			try {
				ps = ConnectionProductCat.getConnection().prepareStatement(sql);

				// **********************************************************************************************************
				ps.setString(1, productName);
				ps.setString(2, quantityNo);
				ps.setString(3, unitNo);
				ps.setString(4, sellingNo);
				ps.setString(5, vendorName);

				ps.executeUpdate();
				JOptionPane.showMessageDialog(null, vendorName + " has been Updated successfully");
			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				try {
					ConnectionProductCat.getConnection().close();
					ps.close();
				} catch (SQLException exc) {
					exc.printStackTrace();
				}
			}
		}
	}

	// ***********************************************************************************************
	// update inner class
	private class UpdateListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			upDate();
			// refreshing combo and table on pressing the delete table
			allProduct();
		}
	}
	// delete vendor name and it product from the database

	public void deleteVendorName() {
		PreparedStatement ps = null;
		String vendorName = vendorField.getText().toUpperCase().toString().trim();
		String productName = productField.getText().toUpperCase().toString().trim();
		String deleteQry = "Delete from ProductCategories where Vendor =? Or ProductName =?";

		try {
			ps = ConnectionProductCat.getConnection().prepareStatement(deleteQry);

			ps.setString(1, vendorName);
			ps.setString(2, productName);
			ps.execute();
			JOptionPane.showMessageDialog(null, vendorName + " Vendor was deleted Successfully");
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				ConnectionProductCat.getConnection().close();
			} catch (SQLException exc) {
				exc.printStackTrace();
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
					ps = ConnectionProductCat.getConnection().prepareStatement(deleteQry);
					ps.setString(1, vendorId);

					ps.execute();
					allProduct();// refresh the table of products
					vendorIdField.setText("");

				} catch (SQLException exc) {
					exc.printStackTrace();
				} finally {
					try {
						ps.close();
						ConnectionProductCat.getConnection().close();
					} catch (SQLException exc) {
						exc.printStackTrace();
					}
				}
			} else {
				return;
			}

		}

	}

	


}
