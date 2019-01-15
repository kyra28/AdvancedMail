package com.polytech.xml.services;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.polytech.xml.classes.MailThread;

public class MailerImpl implements Mailer{
	private final static String PATH = "C:\\Users\\Antoine\\Dropbox\\Cours\\Polytech\\APP5\\Informatique\\xml\\Projet\\AdvancedMail\\users\\";
	
	private ArrayList<MailThread> mailThreads= new ArrayList<MailThread>();
	
	public MailerImpl(String user) {
		File folder = new File(PATH+user+"\\recu\\mails");
	    for (final File fileEntry : folder.listFiles()) {
	    	try {
	    		JAXBContext jc = JAXBContext.newInstance("com.polytech.xml.classes");
	    		Unmarshaller um = jc.createUnmarshaller();
	    		
	    		MailThread thread = (MailThread) um.unmarshal(fileEntry);
	    		mailThreads.add(thread);
	    	}catch (Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    }
	}
	
	public ArrayList<MailThread> getListMailThread()
	{
		return mailThreads;
	}
}
