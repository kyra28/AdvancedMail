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

public class SendMailPanel extends JPanel implements ActionListener{
	private JTextField recipient = new JTextField("recipient");
	private JTextArea object = new JTextArea(1,50);
	private JButton sendButton = new MyButton("SEND");
	private SendMailItemsPanel itemsPanel = new SendMailItemsPanel();
	
	
	public SendMailPanel(){		
		JLabel contentLabel = new JLabel("Contenu du mail : ");
		
		sendButton.addActionListener(this);
		sendButton.setActionCommand("send");
		
		itemsPanel.setLayout(new BoxLayout(itemsPanel,BoxLayout.PAGE_AXIS));
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		this.add(recipient);
		this.add(object);
		this.add(contentLabel);
		this.add(itemsPanel);
		this.add(sendButton);
		
		//setSize(800,600); setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("send"))
		{
			try {
				send();
			} catch (DatatypeConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	private void send() throws DatatypeConfigurationException
	{
		MailType mailType = new MailType();
		BodyType bodyType = new BodyType();
		MessageType message = new MessageType();
		ResponseType reponse = new ResponseType();
		
		bodyType.setMessage(message);
		bodyType.setResponse(reponse);
		mailType.setBody(bodyType);

		HeaderType header = new HeaderType(); 
	    GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		header.setDate(now);
		header.setObject(object.getText());
		header.setSender(MailBoxFrame.user);
		header.setRecipient(recipient.getText());
		header.setXsdFileName("noneForNow");
		
		mailType.setHeader(header);
		MailThread thread = new MailThread();
		thread.setMail(mailType);
		
		String pathXML="C:\\Users\\Antoine\\Dropbox\\Cours\\Polytech\\APP5\\Informatique\\xml\\Projet\\AdvancedMail\\users\\"+MailBoxFrame.user+"\\recu\\mails\\";
		String pathXSD="C:\\Users\\Antoine\\Dropbox\\Cours\\Polytech\\APP5\\Informatique\\xml\\Projet\\AdvancedMail\\users\\"+MailBoxFrame.user+"\\recu\\xsd\\";
		try 
		{
			JAXBContext context = JAXBContext.newInstance(MailThread.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			m.marshal(thread, new File(pathXML+"test36.xml"));
		} 
		catch (JAXBException ex) 
		{
			ex.printStackTrace();
		}
		
		try {
			modifyXML(pathXML+"test36.xml");
			modifyXSD(pathXSD+"test36.xsd");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private void modifyXML(String file) throws Exception
	{		
		Document doc = DocumentBuilderFactory
	            .newInstance()
	            .newDocumentBuilder()
	            .parse(new InputSource(file));

	    // use xpath to find node to add to
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    Node node = (Node) xPath.evaluate("/mailThread/mail/body/message",
	            doc.getDocumentElement(), XPathConstants.NODE);

	    for (MailItem item : itemsPanel.getItemList())
	    {
	    	Element e = null;
	    	if (item instanceof MailTextItem)
	    	{
	    		e = doc.createElement(item.getType());
	    		e.setTextContent(((MailTextItem) item).getText());
	    	}
	    	else if (item instanceof MailResponseItem)
	    	{
	    		e = doc.createElement(item.getType());
	    	}
	    	else if (item instanceof MailResponsesItem)
	    	{
	    		e = doc.createElement(item.getType());
	    		for (String value : ((MailResponsesItem) item).getValues())
	    		{
	    			Element valueElement = doc.createElement("value");
	    			valueElement.setTextContent(value);
	    			e.appendChild(valueElement);
	    		}
	    	}
	    	node.appendChild(e);
	    }

	    // output
	    TransformerFactory
	        .newInstance()
	        .newTransformer()
	        .transform(new DOMSource(doc.getDocumentElement()), new StreamResult(System.out));

	}

	private void modifyXSD(String file) throws Exception
	{
		String pathUser="C:\\Users\\Antoine\\Dropbox\\Cours\\Polytech\\APP5\\Informatique\\xml\\Projet\\AdvancedMail\\users\\"+MailBoxFrame.user;

		Document doc = DocumentBuilderFactory
	            .newInstance()
	            .newDocumentBuilder()
	            .parse(new InputSource(pathUser+"\\defaultTypes.xsd"));

	    // use xpath to find node to add to
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    Node node = (Node) xPath.evaluate("/schema/complexType[@name=\"responseType\"]/sequence",
	            doc.getDocumentElement(), XPathConstants.NODE);

	    for (MailItem item : itemsPanel.getItemList())
	    {
	    	Element e=null;
	    	if (item instanceof MailTextItem)
	    	{
		    	e = doc.createElement("xs:element");
		    	e.setAttribute("name", item.getType());
			    e.setAttribute("type", "xs:string");
	    	}
	    	else
	    	{
		    	e = doc.createElement("xs:element");
		    	e.setAttribute("name", item.getType());
			    e.setAttribute("type", item.getType());
	    	}


	    	node.appendChild(e);
	    }

	    // output
	    TransformerFactory
	        .newInstance()
	        .newTransformer()
	        .transform(new DOMSource(doc.getDocumentElement()), new StreamResult(System.out));
	}

}
