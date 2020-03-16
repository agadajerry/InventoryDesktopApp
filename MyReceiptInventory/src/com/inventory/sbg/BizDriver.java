package com.inventory.sbg;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class BizDriver  extends JFrame {

	ImageIcon icon, motto;
	public static void main(String[] args) {
		
		new BizDriver();
		tablesInDB();
		

	}
	public BizDriver() {
		
		

		// frame.setBounds(100, 100, 1100, 700);
		setSize(500, 300);
		// setLocation(500, 280);
		setLocationRelativeTo(null);
		dispose();

		String fileLocation = "C:\\BusinessTransaction\\SBG_Customers\\Report\\ProductList\\";
		
		File newFile = new File(fileLocation);
		if(!newFile.exists()) {
			newFile.mkdirs();
		}
		setUndecorated(true);

		// panel
		JPanel panel = new JPanel();

		panel.setLayout(null);
		panel.setBackground(new Color(193, 67, 73));
		// label
		JLabel iconLabel = new JLabel();
				
		Image myIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource
				("/com/inventory/sbg/resource/images/SBGf.png"));
		setIconImage(myIcon);

		icon = new ImageIcon(getClass().getResource
				("/com/inventory/sbg/resource/images/SBGLog.png"));
				
		iconLabel.setIcon(icon);
		//
		
		JLabel mottoLabel = new JLabel();
		motto = new ImageIcon(getClass().getResource
				("/com/inventory/sbg/resource/images/motto.png"));
		mottoLabel.setIcon(motto);
		mottoLabel.setBounds(5, 205, 490, 17);
		iconLabel.setBounds(178, 15, 95, 120);
		
		JLabel label = new JLabel("SBG INTEGRATED SYSTEMS");
		label.setBounds(100, 170, 300, 20);
		label.setFont(new Font("david", Font.BOLD, 18));
		label.setForeground(Color.WHITE);
		
		panel.add(mottoLabel);
		panel.add(label);
		panel.add(iconLabel);
		add(panel);

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(4000);
					MyBusinessHome bizHome = new MyBusinessHome();
					bizHome.setVisible(true);
					dispose();

				} catch (InterruptedException e) {

				}

			}

		}).start();

		setVisible(true);


}
	// tables for various data entry
		// product table creation
		public static void tablesInDB() {
			String productTableUrl = "Create Table If Not Exists ProductCategories"
					+ " (ID	INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT " + ", Vendor	TEXT NOT NULL, "
					+ "ProductName	TEXT NOT NULL UNIQUE," + "Quantity	INTEGER NOT NULL, UnitPrice	INTEGER NOT NULL, "
					+ "SellingPrice	INTEGER NOT NULL, TotalPrice INTEGER NOT NULL)";
			//
			String custQry = "CREATE TABLE if not Exists CustomerList (CustId	Integer NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ "CusName	Varchar(100) NOT NULL, CusAddress	Varchar(100) NOT NULL, " + "Date dateTime NOT NULL);";
			// customer log table statement
			String logQry = "CREATE TABLE if not Exists CustomerLog (Product_Name Text NOT NULL, "
					+ "Quantity_Bought	Int NOT NULL, SellingPrice int NOT NULL, TotalAmount int NOT NULL, Date	Text NOT NULL);";

			String dbURL = "jdbc:sqlite:C:\\BusinessTransaction\\BusinessInventoryAndReceipt.db";

			// item purchase sqlite query
			
			String itemboughtUrl = "Create Table If Not Exists ItemPurchased ( ProductName TEXT NOT NULL,"
					+ "Quantity	INTEGER NOT NULL, UnitPrice	INTEGER NOT NULL, TotalPrice int NOT NUll)";
			try {
				Connection conn = DriverManager.getConnection(dbURL);
				Statement st, customerSt, logst, itemSt, productSt = null;
				st = conn.createStatement();
				customerSt = conn.createStatement();
				itemSt = conn.createStatement();
				productSt = conn.createStatement();
				//
				customerSt.executeUpdate(custQry);
				
				productSt.executeUpdate(itemboughtUrl);

				//
				logst = conn.createStatement();

				st.executeUpdate(productTableUrl);
				logst.executeUpdate(logQry);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

		}
		//
}

