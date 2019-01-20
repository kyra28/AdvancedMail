package com.polytech.xml.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.polytech.xml.services.MailItem;
import com.polytech.xml.services.MailResponseItem;
import com.polytech.xml.services.MailResponsesItem;
import com.polytech.xml.services.MailTextItem;
import com.polytech.xml.services.XSDService;
import com.polytech.xml.services.XSDType;

public class SendMailItemsPanel extends JPanel implements ActionListener{
	private JButton addResponseButton = new MyButton("ADD Response");
	private JComboBox responseTypeCombo;
	private JButton addTextButton = new MyButton("ADD TEXT");
	private Map<String,String> responseTypeMap = new HashMap<String,String>();
	
	private ArrayList<SendMailItemsItemPanel> itemPanelList = new ArrayList<SendMailItemsItemPanel>();
	
	public SendMailItemsPanel(){
		init();
				
		addResponseButton.addActionListener(this);
		addResponseButton.setActionCommand("addResponse");
		
		addTextButton.addActionListener(this);
		addTextButton.setActionCommand("addText");
		
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		this.add(responseTypeCombo);
		this.add(addResponseButton);
		this.add(addTextButton);
		
		//setSize(800,600); setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addResponse"))
			addResponse(responseTypeMap.get((String)responseTypeCombo.getSelectedItem()));
		if (e.getActionCommand().equals("addText"))
			addText();
		
	}
	
	private void addText()
	{
		SendMailItemTextPanel textPanel = new SendMailItemTextPanel();
		itemPanelList.add(textPanel);
		this.add(textPanel);
		this.revalidate();
	}
	
	private void addResponse(String type)
	{
		SendMailItemsItemPanel response;
		if (type.equals("chooseType") || type.equals("selectorType"))
			response = new SendMailItemResponsesPanel(type);
		else
			response = new SendMailItemResponsePanel(type);
		itemPanelList.add(response);
		this.add(response);
		this.revalidate();
	}
	
	private void init()
	{
		XSDService xsdService = new XSDService();
		ArrayList<XSDType> typeList = null;
		try {
			typeList=xsdService.getTypes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (XSDType type : typeList)
		{
			responseTypeMap.put(type.getName(), type.getId());
		}

		String[] responseTypeStringValues =  new String[6];
		responseTypeMap.keySet().toArray(responseTypeStringValues);
		responseTypeCombo = new JComboBox<String>(responseTypeStringValues);
	}
	
	public List<MailItem> getItemList()
	{
		List<MailItem> itemList = new ArrayList<MailItem>();
		
		for (SendMailItemsItemPanel item : itemPanelList)
		{
			if (item instanceof SendMailItemTextPanel)
			{
				MailItem textItem = new MailTextItem(((SendMailItemTextPanel) item).getText());
				itemList.add(textItem);
			}
			else if (item instanceof SendMailItemResponsePanel)
			{
				MailItem responseItem = new MailResponseItem(item.getType());
				itemList.add(responseItem);
			}
			else if (item instanceof SendMailItemResponsesPanel)
			{
				MailItem responsesItem = new MailResponsesItem(item.getType(),((SendMailItemResponsesPanel) item).getValueList());
				itemList.add(responsesItem);
			}
		}
		return itemList;
	}



}
