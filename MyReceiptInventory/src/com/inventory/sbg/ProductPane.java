package com.inventory.sbg;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


public class ProductPane  extends JPanel{

	private static final long serialVersionUID = 1L;
	private JTextField productField, quantityField, unitField, sellingField, vendorField;
	private JPanel centerPanel;
	private JButton addB, updateB, clearBtn,dbTable;
	 
	private JTable table;
	private DefaultTableModel model;
	private JLabel southLabel;

	public ProductPane() {

		// productTable();

		setLayout(new BorderLayout());

		initUI();
		// allProduct();

	}

	private void initUI() {

		// north panel that hold title label
		JPanel northContainer = new JPanel(new GridLayout(1, 0, 20, 20));
		JLabel titleLabel = new JLabel();
		titleLabel.setText("<html><h3 style = color:rgb(255,255,255)>"
				+ "SBG INTEGRATED SYSTEMS-- STOCK KEEPING AND PRODUCTS INFORMATION MONITORING<hr /></h3><html>");

		
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		northContainer.add(titleLabel);
		northContainer.setBackground(new Color(74,140,73));
		// center_Header is the description of panel that host center_panel

		JPanel centerHeader = new JPanel(new BorderLayout());
		centerHeader.setBackground(new Color(240, 98, 146));

		centerPanel = new JPanel(new GridLayout(10, 2, 5, 5));
		
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
		productField.setPreferredSize(new Dimension(20, 30));
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
		centerHeader.setBorder(new EmptyBorder(20, 10, 10, 30));

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
		
		clearBtn = new JButton("CLEAR TABLE");
		clearBtn.setPreferredSize(new Dimension(100, 30));
		clearBtn.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		buttonPanel.add(clearBtn);
		clearBtn.addActionListener(new ClearTableListener());
		buttonPanel.setBackground(new Color(240, 98, 146));
		buttonPanel.setBorder(new EmptyBorder(40, 20, 40, 20));

		// adding the panel to the panel header

		centerHeader.add(buttonPanel, BorderLayout.SOUTH);
		
		
		add(northContainer, BorderLayout.NORTH);
		add(new CenterImage(), BorderLayout.CENTER);
		add(centerHeader, BorderLayout.WEST);

		// *******************************************************************************************

		// table of product added to the database
		// this table display newly added vendor and items currently in the database
		String ColumnName[] = { "VendorName", "ProductName", "Quantity", "UnitPrice", "TotalPrice" };

		table = new JTable();
		table.setShowVerticalLines(false);
		table.setBackground(Color.ORANGE);
		table.addMouseListener(new TableMouseClick());
		// table.setBackground(Color.RED);
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(ColumnName);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(600, 200));

		// *******************************************************************************

		
		// south panel for table of data from the database
		JPanel eastPanel = new JPanel(new BorderLayout());
		eastPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


		dbTable = new JButton("SHOW PRODUCTS IN STORAGE");
		dbTable.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		dbTable.setForeground(Color.RED);
		
		
		
		dbTable.addActionListener(new ProductListInDBListener());
		
		JPanel pageStartPanel = new JPanel();
		pageStartPanel.add(dbTable);
		pageStartPanel.add(new JLabel(""));
		eastPanel.add(pageStartPanel, BorderLayout.PAGE_START);
		eastPanel.add(scroll, BorderLayout.CENTER);
		
		eastPanel.setBackground(new Color(240, 98, 146));

		add(eastPanel, BorderLayout.EAST);
		// a new southPanel();
		
		
		
		add(new SouthPanel(), BorderLayout.SOUTH);

	}

	// adding item to the data base --this method addNewItem
	public void addNewItem() {

		//

		String vendorName = vendorField.getText().toString().toUpperCase().trim();
		String productName = productField.getText().toString().toUpperCase().trim();
		String quantityNo = quantityField.getText().toString().toUpperCase().trim();
		String unitNo = unitField.getText().toString().toUpperCase().trim();
		String sellingNo = sellingField.getText().toString().toUpperCase().trim();
		
		
		// condition that check if text box is empty or not
		if (vendorName.isEmpty() || productName.isEmpty() || quantityNo.isEmpty() || unitNo.isEmpty()
				|| sellingNo.isEmpty() || quantityNo.contains(".")) {
			JOptionPane.showMessageDialog(null, "Empty text Field(s)...\n Check The entered Values for error!");
		} else {

			int totalAmount = (Integer.parseInt(quantityNo)*Integer.parseInt(sellingNo));
			
			model.addRow(new String[] { vendorField.getText(), productField.getText(), quantityField.getText(),
					unitField.getText(), sellingField.getText() });
			

			PreparedStatement ps = null;

			String sql = "insert into ProductCategories (Vendor, ProductName, Quantity, UnitPrice, SellingPrice, TotalPrice)"
					+ " Values (?,?,?,?,?,?);";

			try {

				// ************************************************************************************************
				ps = DbConnection.getConnection().prepareStatement(sql);

				ps.setString(1, vendorName);
				ps.setString(2, productName);
				ps.setString(3, quantityNo);
				ps.setString(4, unitNo);
				ps.setString(5, sellingNo);
				ps.setInt(6, totalAmount);
				

				ps.executeUpdate();

				JOptionPane.showMessageDialog(null, "Data Saved Successfully...");
				vendorField.setText("");
				productField.setText("");
				quantityField.setText("");
				unitField.setText("");
				sellingField.setText("");

			} catch (SQLException sqlx) {
				JOptionPane.showMessageDialog(null,
						"The Vendor" + " name entered already in the database..");
			} finally {
				try {
					DbConnection.getConnection().close();
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
				ps = DbConnection.getConnection().prepareStatement(sql);

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
					DbConnection.getConnection().close();
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
			//allProduct();
		}
	}
	// delete vendor name and it product from the database

	
	
	/*
	 * this class populate textfields when table row is clicked
	 * 
	 */
	private class TableMouseClick implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {

			int i = table.getSelectedRow();
			//vendorIdField.setText(model.getValueAt(i, 0).toString());
			vendorField.setText(model.getValueAt(i, 0).toString());
			productField.setText(model.getValueAt(i, 1).toString());
			quantityField.setText(model.getValueAt(i, 2).toString());
			unitField.setText(model.getValueAt(i, 3).toString());
			 sellingField.setText(model.getValueAt(i, 4).toString());

		}

		@Override
		public void mousePressed(MouseEvent e) {
			

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	/*
	 * this class display product name, venor and prices from database
	 */
	private class ProductListInDBListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			ProductTablePane pPanel = new ProductTablePane();
			pPanel.setVisible(true);
			
		}

		}

	// clear the items on the table after update
	private class ClearTableListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			int choice = JOptionPane.showConfirmDialog(null, "Do you want to clear Table?");
			if(choice== JOptionPane.YES_OPTION) {
				model.setRowCount(0);// this model empty table row

			}
			
		}
		
	}
}

