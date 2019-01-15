package com.polytech.xml.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
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
import com.polytech.xml.classes.EmptyType;
import com.polytech.xml.classes.ExpectedResponseType;
import com.polytech.xml.classes.MailType;
import com.polytech.xml.classes.ValuesType;

public class SendMailResponsesPanel extends JPanel implements ActionListener{
	private JButton addResponseButton = new MyButton("ADD Response");
	private JComboBox responseTypeCombo;
	private Map<String,String> responseTypeMap = new HashMap<String,String>();
	
	private ArrayList<SendMailResponsesResponsePanel> responsePanelList = new ArrayList<SendMailResponsesResponsePanel>();
	
	public SendMailResponsesPanel(){
		init();
		
		JLabel responseLabel = new JLabel("La/les réponse(s) que vous attendez : ");
		
		addResponseButton.addActionListener(this);
		addResponseButton.setActionCommand("addResponse");
		
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		this.add(responseLabel);
		this.add(responseTypeCombo);
		this.add(addResponseButton);
		
		//setSize(800,600); setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addResponse"))
			addResponse(responseTypeMap.get((String)responseTypeCombo.getSelectedItem()));
		
	}
	
	private void addResponse(String type)
	{
		SendMailResponsesResponsePanel response = new SendMailResponsesResponsePanel(type);
		responsePanelList.add(response);
		this.add(response);
		this.revalidate();
	}
	
	private void init()
	{
		responseTypeMap.put("Libre", "free");
		responseTypeMap.put("Numeric", "num");
		responseTypeMap.put("Date", "date");
		responseTypeMap.put("Telephone", "tel");
		responseTypeMap.put("Choix", "choice");
		responseTypeMap.put("Selection", "selector");
		String[] responseTypeStringValues =  new String[6];
		responseTypeMap.keySet().toArray(responseTypeStringValues);
		responseTypeCombo = new JComboBox<String>(responseTypeStringValues);
	}
	
	public ArrayList<BlockType> getBlocks()
	{
		ArrayList<BlockType> blocktypeList = new ArrayList<BlockType>();
		for (SendMailResponsesResponsePanel responsePanel : responsePanelList)
		{
			BlockType block = new BlockType();
			block.setText(responsePanel.getQuestion());
			ExpectedResponseType expectedResponse = new ExpectedResponseType();
			MultipleValuesTypePanel multipleValues = responsePanel.getMultipleValuesPanel();
			if (multipleValues==null)
			{
				switch(responsePanel.getType())
				{
				case "free": expectedResponse.setFree(new EmptyType());
					break;
				case "num": expectedResponse.setNum(new EmptyType());
					break;
				case "date": expectedResponse.setDate(new EmptyType());
					break;
				case "tel": expectedResponse.setTel(new EmptyType());
					break;
				}

			}
			else
			{
				ValuesType valuesType = new ValuesType();
				valuesType.getValues().addAll(multipleValues.getValueList());
				switch(responsePanel.getType())
				{
				case "choice": expectedResponse.setChoice(valuesType);
					break;
				case "selector": expectedResponse.setSelector(valuesType);
					break;
				}
				
			}
				
			block.setExpectedResponse(expectedResponse);
			blocktypeList.add(block);
		}
		return blocktypeList;
	}



}
