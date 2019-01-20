package com.polytech.xml.services;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.polytech.xml.classes.MailThread;

public class ApplicationContext{
	public final static String PATH = "C:\\Users\\Antoine\\Dropbox\\Cours\\Polytech\\APP5\\Informatique\\xml\\Projet\\AdvancedMail\\users\\";
	
	public static String user = "";
	
	public static String getMailPath()
	{
		return PATH+user+"\\recu\\mails\\";
	}
	
	public static String getXSDPath()
	{
		return PATH+user+"\\recu\\xsd\\";
	}
	
	public static String getConfPath()
	{
		return PATH+user+"\\";
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		ApplicationContext.user = user;
	}
}
