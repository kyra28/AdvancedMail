package com.polytech.xml.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public abstract class SendMailItemsItemPanel extends JPanel{
	private String type= null;
	
	public SendMailItemsItemPanel(String type){
		this.type = type;
		
	}

	public String getType() {
		return type;
	}

}
