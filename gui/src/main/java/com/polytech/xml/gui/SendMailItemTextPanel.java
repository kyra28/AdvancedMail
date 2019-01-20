package com.polytech.xml.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SendMailItemTextPanel extends SendMailItemsItemPanel{
	private JTextArea textArea = new JTextArea();
	
	public SendMailItemTextPanel(){
		super("text");
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		this.add(textArea);
	}
	
	public String getText()
	{
		return textArea.getText();
	}

}
