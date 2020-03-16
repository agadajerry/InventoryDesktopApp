package com.inventory.sbg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class AboutMe extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutMe() {
		setSize(500, 400);
		// setLocation(500, 280);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);// dont exit dialog till operation finished

		
		setUndecorated(true);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.ORANGE);
		
		panel.setBorder(new EmptyBorder(20,202,20,20));
		JLabel imageLabel = new JLabel();
		//URL urlImage = AboutMe.class.getResource("sbg image/"); 
		ImageIcon icon = new ImageIcon(getClass().getResource
				("/com/inventory/sbg/resource/images/jerry1.png"));
		imageLabel.setIcon(icon);
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		panel.add(imageLabel, BorderLayout.NORTH);
		JLabel msgLabel = new JLabel("<html><h2>My name is Idoko Agada Jerry. I am the developer of this app"
				+ ". I will be contacted via:</h2>"
				+ "<p>idokoidoko4@gmail.com</p><p>08160332264</p></html>");
		msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(msgLabel, BorderLayout.CENTER);
		
		//
		JPanel southP = new JPanel(new FlowLayout());
		southP.setBackground(Color.ORANGE);
		JButton okBtn = new JButton("Ok");
		southP.add(new JLabel(""));
		southP.add(okBtn);
		southP.add(new JLabel(""));
		panel.add(southP,BorderLayout.SOUTH);
		panel.setBorder(new EmptyBorder(20,20,20,20));
		add(panel);
		okBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			dispose();
				
			}
			
		});
		
	
	}
}
