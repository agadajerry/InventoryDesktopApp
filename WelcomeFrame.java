package receipt.print;

import java.awt.Color;
import java.awt.Font;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;




public class WelcomeFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon icon, motto;
	

	public static void main(String[] args)  {
		
	 
		// Font blue = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.UNDERLINE, BaseColor.BLUE);
		
		// WelcomeFrame frame
		new WelcomeFrame();
	}

	public WelcomeFrame() {
		tablesInDB();
		

		// frame.setBounds(100, 100, 1100, 700);
		setSize(500, 300);
		// setLocation(500, 280);
		setLocationRelativeTo(null);
		dispose();

		setUndecorated(true);

		// panel
		JPanel panel = new JPanel();

		panel.setLayout(null);
		panel.setBackground(new Color(193, 67, 73));
		// label
		JLabel iconLabel = new JLabel();
		icon = new ImageIcon(
				"./src/receipt/print/sbg image/SBGLog.JPG");
		iconLabel.setIcon(icon);
		JLabel mottoLabel = new JLabel();
		motto = new ImageIcon("./src/receipt/print/sbg image/motto.JPG");
		mottoLabel.setIcon(motto);
		mottoLabel.setBounds(5, 198, 490, 17);
		iconLabel.setBounds(178, 10, 95, 120);
		JLabel weLabel = new JLabel("WELCOME TO");
		JLabel label = new JLabel("SBG INTEGRATED SYSTEMS");
		label.setBounds(100, 170, 300, 20);
		label.setFont(new Font("david", Font.BOLD, 18));
		label.setForeground(Color.WHITE);
		weLabel.setForeground(new Color(0, 255, 255));
		weLabel.setBounds(188, 140, 100, 20);
		panel.add(weLabel);
		panel.add(mottoLabel);
		panel.add(label);
		panel.add(iconLabel);
		add(panel);

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(6000);
					BusinessHome bizHome = new BusinessHome();
					bizHome.setVisible(true);
					// frame1.dispose();
					dispose();

				} catch (InterruptedException e) {

				}

			}

		}).start();

		setVisible(true);

	}

	// tables for various data entry
	// product table creation
	public void tablesInDB() {
		String productTableUrl = "Create Table If Not Exists ProductCategories"
				+ " (ID	INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT " + ", Vendor	TEXT NOT NULL, "
				+ "ProductName	TEXT NOT NULL UNIQUE," + "Quantity	INTEGER NOT NULL, UnitPrice	INTEGER NOT NULL, "
				+ "SellingPrice	INTEGER NOT NULL)";
		//
		String custQry = "CREATE TABLE if not Exists CustomerList (CustId	Integer NOT NULL PRIMARY KEY AUTOINCREMENT, "
				+ "CusName	Varchar(100) NOT NULL, CusAddress	Varchar(100) NOT NULL, " + "Date dateTime NOT NULL);";
		// customer log table statement
		String logQry = "CREATE TABLE if not Exists CustomerLog (Product_Name Text NOT NULL, "
				+ "Quantity_Bought	Int NOT NULL, Date	Text NOT NULL);";

		String dbURL = "jdbc:sqlite:C:\\BusinessTransaction\\BusinessInventoryAndReceipt.db";

		// item purchase sqlite query
		String itemQry = "CREATE TABLE IF NOT EXISTS ItemPurchased ( ProductName TEXT NOT NULL, Quantity 	INTEGER NOT NULL,"
				+ "UnitPrice INTEGER NOT NULL, TotalPrice INTEGER NOT NULL );";
		//
		String itemboughtUrl = "Create Table If Not Exists ItemPurchased ( ProductName TEXT NOT NULL,"
				+ "Quantity	INTEGER NOT NULL, UnitPrice	INTEGER NOT NULL, " + "TotalPrice	INTEGER NOT NULL)";
		try {
			Connection conn = DriverManager.getConnection(dbURL);
			Statement st, customerSt, logst, itemSt, productSt = null;
			st = conn.createStatement();
			customerSt = conn.createStatement();
			itemSt = conn.createStatement();
			productSt = conn.createStatement();
			//
			customerSt.executeUpdate(custQry);
			itemSt.executeUpdate(itemQry);
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
