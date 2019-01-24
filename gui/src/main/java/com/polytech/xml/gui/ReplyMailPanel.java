package com.polytech.xml.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
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

public class ReplyMailPanel extends JPanel{
	private String fileName;
	
	private JLabel recipientLabel = new JLabel("Destinataire : ");
	private JTextArea recipientArea = new JTextArea();
	private JLabel objectLabel = new JLabel("Objet : ");
	private JTextArea objectArea = new JTextArea();
	private JLabel responseLabel = new JLabel("Contenu de la réponse : ");
	private JLabel messageLabel = new JLabel("Contenu du message : ");
	
	
	
	
	public ReplyMailPanel(String fileName, MailThread mail){
		this.fileName = fileName;
		HeaderType header = mail.getMail().getHeader();
		
		recipientArea.setText(header.getSender());
		objectArea.setText("RE: "+header.getObject());
		
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		this.add(recipientLabel);
		this.add(recipientArea);
		this.add(objectLabel);
		this.add(objectArea);
		this.add(responseLabel);
		
		try {
			displayMessage();
			this.add(new SendMailItemsPanel());
		} catch (Exception e) {
			e.printStackTrace();
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
	    		this.add(new JLabel(node.getNodeName()));
	    		JTextField field = new JTextField();
	    		this.add(field);
	    	}
	    	else if (childNode.getNodeType()==Node.TEXT_NODE)
	    		this.add(new JLabel(childNode.getTextContent()));
	    	else if (childNode.getNodeType()==Node.ELEMENT_NODE)
	    	{
	    		while (childNode!=null)
	    		{
	    			JCheckBox check = new JCheckBox(childNode.getTextContent());
	    			this.add(check);
	    			childNode = childNode.getNextSibling();
	    		}
	    	}
	    	node = node.getNextSibling();
	    }
	}
	
}
