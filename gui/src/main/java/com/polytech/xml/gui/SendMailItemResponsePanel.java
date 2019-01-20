package com.polytech.xml.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SendMailItemResponsePanel extends SendMailItemsItemPanel{
	
	public SendMailItemResponsePanel(String type){
		super(type);
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		this.add(new JLabel(type));
		
	}
}
