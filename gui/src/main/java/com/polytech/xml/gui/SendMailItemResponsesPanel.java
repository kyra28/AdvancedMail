package com.polytech.xml.gui;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SendMailItemResponsesPanel extends SendMailItemsItemPanel{

	private MultipleValuesTypePanel multipleValuesPanel = null;
	
	public SendMailItemResponsesPanel(String type){
		super(type);
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

		multipleValuesPanel = new MultipleValuesTypePanel();
		this.add(multipleValuesPanel);
		
	}

	public List<String> getValueList() {
		return multipleValuesPanel.getValueList();
	}

}
