package com.polytech.xml.services;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilderFactory;
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

import com.polytech.xml.classes.BodyType;
import com.polytech.xml.classes.HeaderType;
import com.polytech.xml.classes.MailThread;
import com.polytech.xml.classes.MailType;
import com.polytech.xml.classes.MessageType;
import com.polytech.xml.classes.OldMailsType;
import com.polytech.xml.classes.ResponseType;

public class MailerImpl implements Mailer{
	private String user;
	//private final static String PATH = "C:\\Users\\Antoine\\Dropbox\\Cours\\Polytech\\APP5\\Informatique\\xml\\Projet\\AdvancedMail\\users\\";
	
	public MailerImpl(String user) {
		this.user=user;
	}

	public void send(HeaderType header, List<MailItem> itemList) throws DatatypeConfigurationException
	{
		String fileId= ApplicationContext.getFileId();
		String xsdFile= ApplicationContext.getXSDPath(header.getRecipient())+fileId+".xsd";
		String xmlFile = ApplicationContext.getMailPath(header.getRecipient())+fileId+".xml";
		
		MailType mailType = new MailType();
		BodyType bodyType = new BodyType();
		MessageType message = new MessageType();
		ResponseType reponse = new ResponseType();
		
		bodyType.setMessage(message);
		bodyType.setResponse(reponse);
		mailType.setBody(bodyType);

	    GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		header.setDate(now);
		header.setObject(header.getObject());
		header.setSender(header.getSender());
		header.setRecipient(header.getRecipient());
		
		mailType.setHeader(header);
		MailThread thread = new MailThread();
		thread.setMail(mailType);
		
		try 
		{
			JAXBContext context = JAXBContext.newInstance(MailThread.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			m.marshal(thread, new File(xmlFile));
		} 
		catch (JAXBException ex) 
		{
			ex.printStackTrace();
		}
		
		try {
			modifyXMLMessage(xmlFile,itemList);
			modifyXSD(xsdFile,itemList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public void reply(String fileName, HeaderType header, List<MailItem> itemList, List<String> stringValues, List<List<Integer>> checkedBoxList, List<Integer> selectedButtonList) throws Exception
	{
		String fileId = ApplicationContext.getFileId();
		String xsdFile= ApplicationContext.getXSDPath(header.getRecipient())+fileId+".xsd";
		String xmlFile = ApplicationContext.getMailPath(header.getRecipient())+fileId+".xml";
		
		Document doc = DocumentBuilderFactory
	            .newInstance()
	            .newDocumentBuilder()
	            .parse(new InputSource(ApplicationContext.getMailPath()+fileName));

	    XPath xPath = XPathFactory.newInstance().newXPath();
	    
	    Node oldMails = (Node) xPath.evaluate("/mailThread/oldMails",
	            doc.getDocumentElement(), XPathConstants.NODE);
	    Node mail = (Node) xPath.evaluate("/mailThread/mail",
	            doc.getDocumentElement(), XPathConstants.NODE);
	    
	    if (oldMails==null)
	    {
	    	oldMails=doc.createElement("oldMails");
	    	oldMails.appendChild(mail.cloneNode(true));
	    	doc.getDocumentElement().appendChild(oldMails);
	    }
	    else
	    {
	    	oldMails.insertBefore(mail, oldMails.getFirstChild());
	    }
	    
	    Node headerNode = (Node) xPath.evaluate("/mailThread/mail/header",
	            doc.getDocumentElement(), XPathConstants.NODE);
	    
	    while(headerNode.hasChildNodes())
	    	headerNode.removeChild(headerNode.getFirstChild());
	    
	    GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        
	    Element sender = doc.createElement("sender");
	    Element recipient = doc.createElement("recipient");
	    Element object = doc.createElement("object");
	    Element date = doc.createElement("date");
	    sender.setTextContent(header.getSender());
	    recipient.setTextContent(header.getRecipient());
	    object.setTextContent(header.getObject());
	    date.setTextContent(now.toXMLFormat());
	    headerNode.appendChild(sender);
	    headerNode.appendChild(recipient);
	    headerNode.appendChild(object);
	    headerNode.appendChild(date);
		
	    // output
	    TransformerFactory
	        .newInstance()
	        .newTransformer()
	        .transform(new DOMSource(doc.getDocumentElement()), new StreamResult(new File(xmlFile)));

		
		try {
			modifyXMLResponse(xmlFile,stringValues,checkedBoxList, selectedButtonList);
			//modifyXMLMessage(xmlFile,itemList);
			//modifyXSD(xsdFile,itemList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	private void modifyXMLResponse(String filePath, List<String> stringValues, List<List<Integer>> checkedBoxList, List<Integer> selectedButtonList) throws Exception
	{
		System.out.println(filePath);
		Document doc = DocumentBuilderFactory
	            .newInstance()
	            .newDocumentBuilder()
	            .parse(new InputSource(filePath));

	    // use xpath to find node to add to
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    Node responseNode = (Node) xPath.evaluate("/mailThread/mail/body/response",
	            doc.getDocumentElement(), XPathConstants.NODE);
	    
	    Node messageNode = ((Node) xPath.evaluate("/mailThread/mail/body/message",
	            doc.getDocumentElement(), XPathConstants.NODE)).cloneNode(true);

	    System.out.println("fils : "+messageNode.getChildNodes().getLength());
	    NodeList testList = messageNode.getChildNodes();
	    for (int i=0 ; i<testList.getLength();i++)
	    {
	    	System.out.println(i + " : "+testList.item(i).getNodeName());
	    }
	    Node messageChildNode = messageNode.getFirstChild();
	    while(messageChildNode!=null)
	    {
	    	System.out.println("name : "+messageChildNode.getNodeName());
	    	System.out.println(messageChildNode.getFirstChild().getNodeType());
	    	if (messageChildNode.getNodeType()==Node.TEXT_NODE)
	    	{
	    		System.out.println("child TXT");
	    		messageChildNode.setTextContent(stringValues.remove(0));
	    	}
	    	else if (messageChildNode.getNodeType()==Node.ELEMENT_NODE)
	    	{
	    		System.out.println("child ELT");
	    		if (messageChildNode.getNodeName().equals("choice"))
	    		{
	    			((Element)messageChildNode).setAttribute("selected", Integer.toString(selectedButtonList.remove(0)));
	    		}
	    		else if(messageChildNode.getNodeName().equals("selector"))
	    		{
	    			NodeList valueNodeList = messageChildNode.getChildNodes();
	    			ArrayList<Integer> ids = (ArrayList<Integer>) checkedBoxList.remove(0);
	    			for (int i=0; i<ids.size();i++)
	    			{
	    				if (ids.contains(i))
	    					((Element)valueNodeList.item(i)).setAttribute("selected", "true");
	    				else
	    					((Element)valueNodeList.item(i)).setAttribute("selected", "false");
	    			}
	    		}
	    	}
	    	
	    	responseNode.appendChild(messageChildNode);
	    	messageChildNode = messageChildNode.getNextSibling();
	    }

	    // output
	    TransformerFactory
	        .newInstance()
	        .newTransformer()
	        .transform(new DOMSource(doc.getDocumentElement()), new StreamResult(new File(filePath)));
	}
	
	private void modifyXMLMessage(String filePath, List<MailItem> itemList) throws Exception
	{		
		System.out.println(filePath);
		Document doc = DocumentBuilderFactory
	            .newInstance()
	            .newDocumentBuilder()
	            .parse(new InputSource(filePath));

	    // use xpath to find node to add to
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    Node node = (Node) xPath.evaluate("/mailThread/mail/body/message",
	            doc.getDocumentElement(), XPathConstants.NODE);
	    while(node.hasChildNodes())
	    	node.removeChild(node.getFirstChild());
	    
	    for (MailItem item : itemList)
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
	        .transform(new DOMSource(doc.getDocumentElement()),  new StreamResult(new File(filePath)));

	}

	private void modifyXSD(String filePath, List<MailItem> itemList) throws Exception
	{
		Document doc = DocumentBuilderFactory
	            .newInstance()
	            .newDocumentBuilder()
	            .parse(new InputSource(ApplicationContext.getConfPath()+"defaultTypes.xsd"));

	    // use xpath to find node to add to
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    Node node = (Node) xPath.evaluate("/schema/complexType[@name=\"responseType\"]/sequence",
	            doc.getDocumentElement(), XPathConstants.NODE);

	    for (MailItem item : itemList)
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
	        .transform(new DOMSource(doc.getDocumentElement()),  new StreamResult(new File(filePath)));
	}
	
	public Map<String,MailThread> getListMailThread()
	{
		Map<String,MailThread> mailThreads= new HashMap<String,MailThread>();
		File folder = new File(ApplicationContext.getMailPath());
	    for (final File fileEntry : folder.listFiles()) {
	    	try {
	    		JAXBContext jc = JAXBContext.newInstance("com.polytech.xml.classes");
	    		Unmarshaller um = jc.createUnmarshaller();
	    		
	    		MailThread thread = (MailThread) um.unmarshal(fileEntry);
	    		mailThreads.put(fileEntry.getName(),thread);
	    	}catch (Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    }
	    return mailThreads;
	}
	
	public MailThread getMailThread(String fileName)
	{
		File file = new File(ApplicationContext.getMailPath()+fileName);
		MailThread thread = null;
    	try {
    		JAXBContext jc = JAXBContext.newInstance("com.polytech.xml.classes");
    		Unmarshaller um = jc.createUnmarshaller();
    		
    		thread = (MailThread) um.unmarshal(file);
    	}catch (Exception e)
    	{
    		e.printStackTrace();
    	}
	    return thread;
	}
}
