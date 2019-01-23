package com.polytech.xml.services;

import java.util.ArrayList;

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

public class XSDService {
	
	public XSDService()
	{
		
	}
	
	public ArrayList<XSDType> getTypes() throws Exception
	{
		ArrayList<XSDType> types = new ArrayList<>();
		
		Document doc = DocumentBuilderFactory
	            .newInstance()
	            .newDocumentBuilder()
	            .parse(new InputSource(ApplicationContext.getConfPath()+"defaultTypes.xsd"));

	    // use xpath to find node to add to
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    String annotation = (String) xPath.evaluate("/schema/annotation/documentation/text()",
	            doc.getDocumentElement(), XPathConstants.STRING);
	    
	    annotation = annotation.replaceAll("\\s", "");
	    annotation = annotation.replace("\\n", "d");
	    annotation = annotation.replaceAll("\\t", "");
	    for (String type : annotation.split(";"))
	    {
	    	String[] typeSplit = type.split("-");
	    	types.add(new XSDType(typeSplit[0],typeSplit[1]));
	    }
		return types;
	}
	
	public ArrayList<XSDType> sample() throws Exception
	{
		ArrayList<XSDType> types = new ArrayList<>();
		
		Document doc = DocumentBuilderFactory
	            .newInstance()
	            .newDocumentBuilder()
	            .parse(new InputSource("test.xml"));

	    // use xpath to find node to add to
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    NodeList nodes = (NodeList) xPath.evaluate("/schema/complexType[@name=\"Containter1\"]",
	            doc.getDocumentElement(), XPathConstants.NODESET);

	    // create element to add
	    Element newElement = doc.createElement("xs:element");
	    newElement.setAttribute("type", "element3");

	    nodes.item(0).appendChild(newElement);


	    // output
	    TransformerFactory
	        .newInstance()
	        .newTransformer()
	        .transform(new DOMSource(doc.getDocumentElement()), new StreamResult(System.out));
	    
		return types;
	}
}
