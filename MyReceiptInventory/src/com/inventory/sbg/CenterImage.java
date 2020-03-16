package com.inventory.sbg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class CenterImage extends JPanel implements ActionListener{

	private int x =getWidth()/2;
	private int y = getHeight()/2;
	private Image imageIcon, newImage;
	private Timer timer;
	
	public CenterImage() {
		
		
		ImageIcon image = new ImageIcon(getClass().getResource("/com/inventory/sbg/resource/images/myProduct.png"));
		ImageIcon saleIcon = new ImageIcon(getClass().getResource("/com/inventory/sbg/resource/images/SBGLog.png"));

		setBorder(new EmptyBorder(20,20,20,20));
		
		imageIcon = saleIcon.getImage();
		newImage = image.getImage();
		
		
		timer = new Timer(26, this);
        timer.start();
        
		
	}
	
	public void paintComponent(Graphics g ) {
		super.paintComponent(g);
		g.setColor(new Color(74,140,73));
		g.fillRect(0, 0, getWidth(), getHeight());
		drawImage(g);
	}
	private void drawImage(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawImage(imageIcon, x, 0, this);
		
		
		g.setColor(Color.black);
		g.drawImage(newImage, 2, y, this);
		
		g.drawImage(imageIcon, -x, 0, this);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		 x += 1;
	     y += 1;	

		if(y > getHeight()) {
			x=10;
			y =10;
		}
		repaint();
		
		
	}
}
