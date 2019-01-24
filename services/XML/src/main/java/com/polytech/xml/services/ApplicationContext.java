package com.polytech.xml.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
		return getMailPath(user);
	}
	
	public static String getMailPath(String recipient)
	{
		return PATH+recipient+"\\recu\\mails\\";
	}
	
	
	public static String getXSDPath()
	{
		return getXSDPath(user);
	}
	
	public static String getXSDPath(String recipient)
	{
		return PATH+recipient+"\\recu\\xsd\\";
	}
	
	public static String getSendMailPath()
	{
		return getSendMailPath(user);
	}
	
	public static String getSendMailPath(String recipient)
	{
		return PATH+recipient+"\\envoye\\mails\\";
	}
	
	
	public static String getSendXSDPath()
	{
		return getSendXSDPath(user);
	}
	
	public static String getSendXSDPath(String recipient)
	{
		return PATH+recipient+"\\envoye\\xsd\\";
	}
	
	
	public static String getFileId()
	{
		String fileId = "";
		
		BufferedWriter out = null;
	    try {
	        BufferedReader br = new BufferedReader(new FileReader(PATH+"id"));
	        int fileIdNumber = 0;
	        String temp = "";
	        while ((temp = br.readLine()) != null) {
	        	fileId=temp;
	            fileIdNumber=(Integer.parseInt(fileId));
	        }

	        out = new BufferedWriter(new FileWriter(PATH+"id", false));
	        out.write(String.valueOf(fileIdNumber+1));

	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (out != null) {
	            try {
	                out.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
		return fileId;
	}
	
	public static String getConfPath()
	{
		return PATH+ApplicationContext.getUser()+"\\";
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		ApplicationContext.user = user;
	}
}
