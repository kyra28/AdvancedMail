package com.polytech.xml.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
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

import com.polytech.xml.classes.BlockType;
import com.polytech.xml.classes.BodyType;
import com.polytech.xml.classes.HeaderType;
import com.polytech.xml.classes.MailThread;
import com.polytech.xml.classes.MailType;

public class SendMailPanel extends JPanel implements ActionListener{
	private JTextField recipient = new JTextField("recipient");
	private JTextArea object = new JTextArea(1,50);
	private JButton sendButton = new MyButton("SEND");
	private SendMailResponsesPanel responsesPanel = new SendMailResponsesPanel();
	
	public SendMailPanel(){		
		JLabel contentLabel = new JLabel("Contenu du mail : ");
		
		sendButton.addActionListener(this);
		sendButton.setActionCommand("send");
		
		responsesPanel.setLayout(new BoxLayout(responsesPanel,BoxLayout.PAGE_AXIS));
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		this.add(recipient);
		this.add(object);
		this.add(contentLabel);
		this.add(responsesPanel);
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
		ArrayList<BlockType> blocks = new ArrayList<BlockType>();
		
		blocks.addAll(responsesPanel.getBlocks());

		bodyType.getBlock().addAll(blocks);
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
		
		try 
		{
			JAXBContext context = JAXBContext.newInstance(MailThread.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			String path="C:\\Users\\Antoine\\Dropbox\\Cours\\Polytech\\APP5\\Informatique\\xml\\Projet\\AdvancedMail\\users\\"+MailBoxFrame.user+"\\recu\\mails\\";
			m.marshal(thread, new File(path+"test2.xml"));
		} 
		catch (JAXBException ex) 
		{
			ex.printStackTrace();
		}
		
	}



}
