package com.inventory.sbg;

import java.awt.BorderLayout;
import java.awt.Color;
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



public class MyBusinessHome extends JFrame{

	public MyBusinessHome() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("POINT OF SALES AND INVENTORY MANAGEMENT SYSTEM");
		setSize(1200, 730);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		//URL urlImage = MyBusinessHome.class.getResource("./sbg image/"); 

		Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource
				("/com/inventory/sbg/resource/images/SBGLog.png"));
		setIconImage(icon);

		add(new ProductPane(), BorderLayout.CENTER);
		initUI();
		//customerTable();
		//ItemBoughtTable();//db tables' creation when the program start
	}

	private void initUI() {
		
		

		// menu bar and items variab;es
		JMenuBar mBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		
		mBar.add(menu);
		mBar.add(editMenu);
		mBar.setBackground(new Color(240, 98, 146));
		
		JMenuItem saleItem = new JMenuItem("SALE");
		JMenuItem customerItem = new JMenuItem("Customer List");
		JMenuItem saleHistory = new JMenuItem("Sale History");
		JMenuItem save = new JMenuItem("Open Saved files");
		JMenuItem aboutDev = new JMenuItem("About Dev");
		customerItem.setMnemonic(KeyEvent.VK_C);
		saleHistory.setMnemonic(KeyEvent.VK_H);
		save.setMnemonic(KeyEvent.VK_S);
		saleItem.setMnemonic(KeyEvent.VK_S);
		menu.add(saleItem);
		menu.add(customerItem);
		menu.add(saleHistory);
		menu.addSeparator();
		menu.add(save);
		menu.addSeparator();
		menu.add(aboutDev);
		//******************************************************************************
		
		//****************************************************************************************8

		setJMenuBar(mBar);
		
		customerItem.addActionListener(new CustomerPanel());
		save.addActionListener(new SavePanel());
		aboutDev.addActionListener(new AboutListener());

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
				
				HistoryPane sHistory = new HistoryPane();
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
			CustomerInfo cusFrame = new CustomerInfo();
		
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
				Logger.getLogger(MyBusinessHome.class.getName()).log(Level.SEVERE,null,ex);
				
			}
		}
		
	}
	private class AboutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			AboutMe aDev = new AboutMe();
			aDev.setVisible(true);
			
		}
		
	}
	

}

