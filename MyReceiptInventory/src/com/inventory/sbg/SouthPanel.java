package com.inventory.sbg;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class SouthPanel extends JPanel{

	private JLabel southLabel;
	
	
	
	public SouthPanel() {
		
		
		setLayout(new FlowLayout());
		JPanel westPanel = new JPanel();

		westPanel.setBorder(new EmptyBorder(20,20,20,20));
		westPanel.setBackground(new Color(74,140,73));
		setBackground(new Color(74,140,73));

			
			
			 
		/*
		 * string array for displaying texts on the south panel
		 */
		String [] textList = {"SBG INTEGRATED SYSTEMS"
				, "LISTS OF PRODUCTS ARE SAVED IN THE DATABASE", " STOCK KEEPING AND PRODUCTS INFORMATION MONITORING",
				"Designed by JerrySoft"};
		southLabel = new JLabel();
		southLabel.setForeground(Color.WHITE);
		
		southLabel.setFont(new Font("David",Font.BOLD,22));
		//add(southLabel);
		westPanel.add(southLabel, SwingConstants.HORIZONTAL);
		
				add(westPanel);
		
		
		
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				for(int i =0;i<200;i++) {
					for(int j =0;j<textList.length;j++) {
						
					southLabel.setText(textList[j]);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
			
		}).start();
		
		
	}
	
}
