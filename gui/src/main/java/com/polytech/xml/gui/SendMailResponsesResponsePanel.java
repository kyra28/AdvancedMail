package com.polytech.xml.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.polytech.xml.classes.BlockType;
import com.polytech.xml.classes.BodyType;
import com.polytech.xml.classes.MailType;

public class SendMailResponsesResponsePanel extends JPanel{
	private String type= null;
	
	private JLabel typeLabel;

	private MultipleValuesTypePanel multipleValuesPanel = null;
	
	public SendMailResponsesResponsePanel(String type){
		this.type = type;
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		this.add(typeLabel);
		
		if (type.equals("choice") || type.equals("selector"))
		{
			multipleValuesPanel = new MultipleValuesTypePanel();
			this.add(multipleValuesPanel);
		}
		
	}

	public String getType() {
		return type;
	}

	public MultipleValuesTypePanel getMultipleValuesPanel() {
		return multipleValuesPanel;
	}

}
