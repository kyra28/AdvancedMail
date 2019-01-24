package com.polytech.xml.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.maven.shared.utils.io.FileUtils;
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
import com.polytech.xml.classes.OldMailsType;
import com.polytech.xml.classes.ResponseType;

public class MailerImpl implements Mailer{
	private String user;
	//private final static String PATH = "C:\\Users\\Antoine\\Dropbox\\Cours\\Polytech\\APP5\\Informatique\\xml\\Projet\\AdvancedMail\\users\\";
	
	public MailerImpl(String user) {
		this.user=user;
	}

	public Boolean send(HeaderType header, List<MailItem> itemList) throws DatatypeConfigurationException
	{
		String fileId= ApplicationContext.getFileId();
		String xsdFile = fileId+".xsd";
		String xmlFile = fileId+".xml";
		String xsdFilePath= ApplicationContext.getXSDPath(header.getRecipient())+xsdFile;
		String xmlFilePath = ApplicationContext.getMailPath(header.getRecipient())+xmlFile;
		
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
			
			m.marshal(thread, new File(xmlFilePath));
		} 
		catch (JAXBException ex) 
		{
			ex.printStackTrace();
		}
		
		try {
			modifyXMLMessage(xmlFilePath,itemList);
			modifyXSD(xsdFilePath,itemList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!validateXML(xmlFilePath, ApplicationContext.getXSDPath()+"defaultTypes.xsd"))
		{
			System.out.println("PAS VALID");
			//new File(xmlFilePath).delete();
			//new File(xsdFilePath).delete();
			return false;
		}
		
		copyFiles(xmlFilePath,ApplicationContext.getSendMailPath()+xmlFile);
		copyFiles(xsdFilePath, ApplicationContext.getSendXSDPath()+xsdFile);
		
		System.out.println("SEND : " +fileId);
		return true;
	}
	
	public Boolean reply(String fileName, HeaderType header, List<MailItem> itemList, List<String> stringValues, List<List<Integer>> checkedBoxList, List<Integer> selectedButtonList) throws Exception
	{
		String fileId = ApplicationContext.getFileId();
		String xsdFile = fileId+".xsd";
		String xmlFile = fileId+".xml";
		String xsdFilePath= ApplicationContext.getXSDPath(header.getRecipient())+xsdFile;
		String xmlFilePath = ApplicationContext.getMailPath(header.getRecipient())+xmlFile;
		
		Document doc = DocumentBuilderFactory
	            .newInstance()
	            .newDocumentBuilder()
	            .parse(new InputSource(ApplicationContext.getMailPath()+fileName));

	    XPath xPath = XPathFactory.newInstance().newXPath();
	    
	    Node oldMails = (Node) xPath.evaluate("/mailThread/oldMails",
	            doc.getDocumentElement(), XPathConstants.NODE);
	    Node mail = (Node) xPath.evaluate("/mailThread/mail",
	            doc.getDocumentElement(), XPathConstants.NODE);
	    
//	    if (oldMails==null)
//	    {
//	    	oldMails=doc.createElement("oldMails");
//	    	oldMails.appendChild(mail.cloneNode(true));
//	    	doc.getDocumentElement().appendChild(oldMails);
//	    }
//	    else
//	    {
//	    	oldMails.insertBefore(mail.cloneNode(true), oldMails.getFirstChild());
//	    }
	    
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
	        .transform(new DOMSource(doc.getDocumentElement()), new StreamResult(new File(xmlFilePath)));

		
		try {
			modifyXMLResponse(xmlFilePath,stringValues,checkedBoxList, selectedButtonList);
			modifyXMLMessage(xmlFilePath,itemList);
			modifyXSD(xsdFilePath,itemList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!validateXML(xmlFilePath, ApplicationContext.getXSDPath()+fileName.replace(".xml", ".xsd")))
		{
			System.out.println("PAS VALID REPLY");
			//new File(xmlFilePath).delete();
			//new File(xsdFilePath).delete();
			return false;
		}
		copyFiles(xmlFilePath,ApplicationContext.getSendMailPath()+xmlFile);
		copyFiles(xsdFilePath, ApplicationContext.getSendXSDPath()+xsdFile);
		
		System.out.println("REPLY : " +fileId);
		return true;
	}
	
	private void copyFiles(String recipientPath, String senderPath) {
		File src = new File(recipientPath);
		File dst = new File(senderPath);
		try {
			FileUtils.copyFile(src, dst);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void modifyXMLResponse(String filePath, List<String> stringValues, List<List<Integer>> checkedBoxList, List<Integer> selectedButtonList) throws Exception
	{
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

	    while(responseNode.hasChildNodes())
	    	responseNode.removeChild(responseNode.getFirstChild());
	    
	    NodeList childList = messageNode.getChildNodes();
	    for (int id=0; id<childList.getLength();id++)
	    {
	    	Node messageChildNode = childList.item(id).cloneNode(true);
	    	if (messageChildNode.getNodeType()==Node.ELEMENT_NODE)
	    	{
	    		if (messageChildNode.getFirstChild() == null)
	    		{
	    			messageChildNode.setTextContent(stringValues.remove(0));
	    		}
	    		else if (messageChildNode.getNodeName().equals("choice"))
	    		{
	    			((Element)messageChildNode).setAttribute("selected", Integer.toString(selectedButtonList.remove(0)));
	    		}
	    		else if(messageChildNode.getNodeName().equals("selector"))
	    		{
	    			NodeList valueNodeList = messageChildNode.getChildNodes();
	    			ArrayList<Integer> ids = (ArrayList<Integer>) checkedBoxList.remove(0);
	    			for (int i=0; i<valueNodeList.getLength();i++)
	    			{
	    				if (ids.contains(i))
	    					((Element)valueNodeList.item(i)).setAttribute("selected", "true");
	    				else
	    					((Element)valueNodeList.item(i)).setAttribute("selected", "false");
	    			}
	    		}
	    	}
	    	
	    	responseNode.appendChild(messageChildNode);
	    }

	    // output
	    TransformerFactory
	        .newInstance()
	        .newTransformer()
	        .transform(new DOMSource(doc.getDocumentElement()), new StreamResult(new File(filePath)));
	}
	
	private void modifyXMLMessage(String filePath, List<MailItem> itemList) throws Exception
	{		
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
		File[] fileList = folder.listFiles();
		//Arrays.sort(fileList);
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
	
	private Boolean validateXML (String xmlFile, String xsdFile)
	{
		File schemaFile = new File(xsdFile);
		// webapp example xsd: 
		// URL schemaFile = new URL("http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd");
		// local file example:
		// File schemaFile = new File("/location/to/localfile.xsd"); // etc.
		Source xml = new StreamSource(new File(xmlFile));
		SchemaFactory schemaFactory = SchemaFactory
		    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
		  Schema schema = schemaFactory.newSchema(schemaFile);
		  Validator validator = schema.newValidator();
		  validator.validate(xml);
		  System.out.println(xml.getSystemId() + " is valid");
		} catch (SAXException e) {
		  System.out.println(xml.getSystemId() + " is NOT valid reason:" + e);
		  return false;
		} catch (IOException e) {return false;}
		return true;	
	}
}
