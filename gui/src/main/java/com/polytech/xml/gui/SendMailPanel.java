package com.polytech.xml.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.polytech.xml.classes.BodyType;
import com.polytech.xml.classes.HeaderType;
import com.polytech.xml.classes.MailThread;
import com.polytech.xml.classes.MailType;
import com.polytech.xml.classes.MessageType;
import com.polytech.xml.classes.ResponseType;
import com.polytech.xml.services.MailItem;
import com.polytech.xml.services.MailResponseItem;
import com.polytech.xml.services.MailResponsesItem;
import com.polytech.xml.services.MailTextItem;
import com.polytech.xml.services.Mailer;
import com.polytech.xml.services.MailerImpl;

public class SendMailPanel extends JPanel implements ActionListener{
	private JTextField recipient = new JTextField("recipient");
	private JTextArea object = new JTextArea(1,50);
	private JButton sendButton = new MyButton("SEND");
	private SendMailItemsPanel itemsPanel = new SendMailItemsPanel();
	private JLabel contentLabel = new JLabel("Contenu du mail : ");
	private JLabel error = new JLabel();
	
	
	
	public SendMailPanel(){		
		sendButton.addActionListener(this);
		sendButton.setActionCommand("send");
		
		itemsPanel.setLayout(new BoxLayout(itemsPanel,BoxLayout.PAGE_AXIS));
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		add();
		
		//setSize(800,600); setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("send"))
		{
			try {
				if (send())
					reset();
			} catch (DatatypeConfigurationException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	private boolean send() throws DatatypeConfigurationException
	{
		MailerImpl mailer = new MailerImpl(MailBoxFrame.user);
		HeaderType header = new HeaderType(); 

		header.setObject(object.getText());
		header.setSender(MailBoxFrame.user);
		header.setRecipient(recipient.getText());
		
		try {
			return mailer.send(header, itemsPanel.getItemList());
		} catch (IOException e) {
			error.setText(e.toString());
		} catch (SAXException e) {
			error.setText(e.toString());
		}
		this.revalidate();
		this.repaint();		
		return false;
		
	}
	
	private void reset()
	{
		object.setText("");
		recipient.setText("");
		itemsPanel = new SendMailItemsPanel();
		
		this.removeAll();
		add();
		this.revalidate();
		this.repaint();		
	}
	
	private void add()
	{
		this.add(error);
		this.add(recipient);
		this.add(object);
		this.add(contentLabel);
		this.add(itemsPanel);
		this.add(sendButton);
	}
}
