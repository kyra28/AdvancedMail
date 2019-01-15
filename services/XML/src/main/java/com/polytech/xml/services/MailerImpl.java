package com.polytech.xml.services;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.polytech.xml.classes.Echange;

public class MailerImpl implements Mailer{
	private final static String PATH = "C:\\Users\\Antoine\\Dropbox\\Cours\\Polytech\\APP5\\Informatique\\xml\\Projet\\AdvancedMail\\users\\";
	
	private ArrayList<Echange> echanges= new ArrayList<Echange>();
	
	public MailerImpl(String user) {
		File folder = new File(PATH+user+"\\recu\\mails");
	    for (final File fileEntry : folder.listFiles()) {
	    	try {
	    		JAXBContext jc = JAXBContext.newInstance("com.polytech.xml.classes");
	    		Unmarshaller um = jc.createUnmarshaller();
	    		
	    		Echange echange = (Echange) um.unmarshal(fileEntry);
	    		echanges.add(echange);
	    	}catch (Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    }
	}
	
	public ArrayList<Echange> getListEchange()
	{
		return echanges;
	}
}
