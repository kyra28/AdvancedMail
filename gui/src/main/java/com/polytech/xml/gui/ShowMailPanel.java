package com.polytech.xml.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.polytech.xml.classes.HeaderType;
import com.polytech.xml.classes.MailThread;
import com.polytech.xml.classes.MessageType;
import com.polytech.xml.classes.ResponseType;
import com.polytech.xml.services.ApplicationContext;
import com.polytech.xml.services.MailerImpl;
import com.polytech.xml.services.XSDType;

public class ShowMailPanel extends JPanel{
	private String fileName;
	
	private JLabel dateLabel = new JLabel();
	
	private JLabel userLabel = new JLabel("Destinataire : ");
	private JTextArea userArea = new JTextArea();
	private JLabel objectLabel = new JLabel("Objet : ");
	private JTextArea objectArea = new JTextArea();
	private JLabel responseLabel = new JLabel("Contenu de la réponse : ");
	private JLabel messageLabel = new JLabel("Contenu du message : ");
	
	private Boolean isReadOnly;
	private List<JTextField> fieldList = new ArrayList<JTextField>();
	private List<List<JCheckBox>> checkedList = new ArrayList<List<JCheckBox>>();
	private List<ButtonGroup> groupList = new ArrayList<ButtonGroup>();
	
	public ShowMailPanel(String fileName, MailThread mail, Boolean isReadOnly){
		this.fileName = fileName;
		this.isReadOnly = isReadOnly;
		HeaderType header = mail.getMail().getHeader();
		
		userArea.setText(header.getSender());
		
		if (isReadOnly)
		{
			objectArea.setText(header.getObject());
			userLabel.setText("Expediteur : ");
			dateLabel.setText("Date : " + header.getDate());
			userArea.setEditable(false); objectArea.setEditable(false);
		}
		else
		{
			objectArea.setText("RE: "+header.getObject());
			userLabel.setText("Destinataire : ");
		}
		
		
		objectLabel.setText("Objet : ");
		
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		this.add(userLabel);
		this.add(userArea);
		if (isReadOnly)
			this.add(dateLabel);
		this.add(objectLabel);
		this.add(objectArea);
		
		try {
			displayResponse();
			displayMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	private void displayResponse() throws Exception
	{
		Document doc = DocumentBuilderFactory
	            .newInstance()
	            .newDocumentBuilder()
	            .parse(new InputSource(ApplicationContext.getMailPath()+fileName));

	    // use xpath to find node to add to
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    Node node = (Node) xPath.evaluate("/mailThread/mail/body/response",
	            doc.getDocumentElement(), XPathConstants.NODE);
	    
	    node = node.getFirstChild();
	    if (node != null)
	    	this.add(responseLabel);
	    	
	    while(node!=null)
	    {
	    	Node childNode = node.getFirstChild();
	    	if (childNode == null)
	    	{
	    		this.add(new JLabel(node.getNodeName()));
	    		JTextField field = new JTextField();
	    		field.setEditable(false);
	    		this.add(field);
	    	}
	    	else if (childNode.getNodeType()==Node.TEXT_NODE)
	    		this.add(new JLabel(childNode.getTextContent()));
	    	else if (childNode.getNodeType()==Node.ELEMENT_NODE)
	    	{
	    		if(node.getNodeName().equals("choice"))
	    		{
		    		Element e = (Element)node;
		    		String selectedAttribute = e.getAttribute("selected");
		    		int id = Integer.parseInt(selectedAttribute);
		    		int compteur = 0;
		    		while (childNode!=null)
		    		{
		    			JRadioButton radio = new JRadioButton(childNode.getFirstChild().getTextContent());
		    			if(id==compteur++)
		    				radio.setSelected(true);
		    			radio.setEnabled(false);
		    			this.add(radio);
		    			childNode = childNode.getNextSibling();
		    		}
		    		
	    		}
	    		else if(node.getNodeName().equals("selector"))
	    		{
	    			
		    		while (childNode!=null)
		    		{
			    		Element e = (Element)childNode;
			    		String selectedAttribute = e.getAttribute("selected");
		    			JCheckBox check = new JCheckBox(childNode.getFirstChild().getTextContent());
		    			if("true".equals(selectedAttribute))
		    				check.setSelected(true);
		    			check.setEnabled(false);
		    			this.add(check);
		    			childNode = childNode.getNextSibling();
		    		}
	    		}
	    	}
	    	node = node.getNextSibling();
	    }
		
	}
	
	private void displayMessage() throws Exception
	{		
		Document doc = DocumentBuilderFactory
	            .newInstance()
	            .newDocumentBuilder()
	            .parse(new InputSource(ApplicationContext.getMailPath()+fileName));

	    // use xpath to find node to add to
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    Node node = (Node) xPath.evaluate("/mailThread/mail/body/message",
	            doc.getDocumentElement(), XPathConstants.NODE);
	    
	    node = node.getFirstChild();
	    if (node != null)
	    	this.add(messageLabel);
	    
	    while (node!=null)
	    {
	    	Node childNode = node.getFirstChild();
	    	if (childNode == null)
	    	{
	    		this.add(new JLabel(node.getNodeName() + " : "));
	    		JTextField field = new JTextField();
	    		fieldList.add(field);
	    		if (isReadOnly)
	    			field.setEditable(false);
	    		this.add(field);
	    	}
	    	else if (childNode.getNodeType()==Node.TEXT_NODE)
	    		this.add(new JLabel(childNode.getTextContent()));
	    	else if (childNode.getNodeType()==Node.ELEMENT_NODE)
	    	{
	    		if(node.getNodeName().equals("selector"))
	    		{
		    		ArrayList<JCheckBox> checks = new ArrayList<JCheckBox>();
		    		while (childNode!=null)
		    		{
		    			JCheckBox check = new JCheckBox(childNode.getTextContent());
		    			checks.add(check);
		    			if (isReadOnly)
		    				check.setEnabled(false);
		    			this.add(check);
		    			childNode = childNode.getNextSibling();
		    		}
		    		checkedList.add(checks);	
	    		}
	    		else if(node.getNodeName().equals("choice"))
	    		{
		    		ButtonGroup group = new ButtonGroup();
		    		while (childNode!=null)
		    		{
		    			JRadioButton radio = new JRadioButton(childNode.getTextContent());
		    			group.add(radio);
		    			if (isReadOnly)
		    				radio.setEnabled(false);
		    			this.add(radio);
		    			childNode = childNode.getNextSibling();
		    		}
		    		groupList.add(group);	
	    		}
	    	}
	    	node = node.getNextSibling();
	    }
	}
	
	public List<String> getValueList()
	{
		List<String> values = new ArrayList<String>();
		for (JTextField field : fieldList)
		{
			values.add(field.getText());
		}
		return values;
	}
	
	public List<List<Integer>> getCheckedList()
	{
		List<List<Integer>> checked = new ArrayList<List<Integer>>();
		
		for (List<JCheckBox> checkBoxList : checkedList)
		{
			List<Integer> checkedBoxList = new ArrayList<Integer>();
			for(JCheckBox check : checkBoxList)
			{
				if (check.isSelected())
					checkedBoxList.add(checkBoxList.indexOf(check));
			}
			checked.add(checkedBoxList);
		}
		return checked;
	}
	
	public List<Integer> getSelectedRadioList()
	{
		List<Integer> list = new ArrayList<Integer>();
		
		for (ButtonGroup buttonGroup : groupList)
		{
			int compteur =0;
	        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) 
	        {
	            AbstractButton button = buttons.nextElement();
	            
	            if (button.isSelected()) {
	                list.add(compteur);
	                break;
	            }
	            compteur++;
	        }
		}
		return list;
	}
	
	public HeaderType getHeader()
	{
		HeaderType header = new HeaderType();
		header.setObject(objectArea.getText());
		header.setSender(MailBoxFrame.user);
		header.setRecipient(userArea.getText());
		return header;
	}
}
