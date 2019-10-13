package receipt.print;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



public class BusinessHome extends JFrame {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessHome() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("POINT OF SALES AND INVENTORY MANAGEMENT");
		setSize(1200, 730);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		Image icon = Toolkit.getDefaultToolkit().getImage("./src/receipt/print/sbg image/sale.JPEG");
		setIconImage(icon);

		add(new ProductPanel(), BorderLayout.CENTER);
		initUI();
		//customerTable();
		//ItemBoughtTable();//db tables' creation when the program start
	}

	private void initUI() {

		// menu bar and items
		JMenuBar mBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		
		mBar.add(menu);
		mBar.add(editMenu);
		
		JMenuItem saleItem = new JMenuItem("SALE");
		JMenuItem customerItem = new JMenuItem("Customer List");
		JMenuItem saleHistory = new JMenuItem("Sale History");
		JMenuItem save = new JMenuItem("Open Saved files");
		customerItem.setMnemonic(KeyEvent.VK_C);
		saleHistory.setMnemonic(KeyEvent.VK_H);
		save.setMnemonic(KeyEvent.VK_S);
		saleItem.setMnemonic(KeyEvent.VK_S);
		menu.add(saleItem);
		menu.add(customerItem);
		menu.add(saleHistory);
		menu.addSeparator();
		menu.add(save);
		//******************************************************************************
		
		//****************************************************************************************8

		setJMenuBar(mBar);
		
		customerItem.addActionListener(new CustomerPanel());
		save.addActionListener(new SavePanel());

		// Action listeners to the various buttons

		// sale menu listener button and action listener class
		saleItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MySalePanel sPanel = new MySalePanel();
				
				sPanel.setVisible(true);
				
				
			}

		});
		
		// open save reciept
		
		// history panel actionListener
		saleHistory.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				HistoryPanel sHistory = new HistoryPanel();
				sHistory.setVisible(true);
				

			}

		});
		// frame visibility
		setVisible(true);

	}

	public class CustomerPanel implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			CustomerFrame cusFrame = new CustomerFrame();
		
			cusFrame.setVisible(true);
		}

	}
	private class SavePanel implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				String receiptFolder = "C:\\BusinessTransaction\\SBG_Customers\\";

				Runtime.getRuntime().exec("explorer  /select," +receiptFolder);
			}catch(IOException ex) {
				Logger.getLogger(BusinessHome.class.getName()).log(Level.SEVERE,null,ex);
				
			}
		}
		
	}
	
	
}
