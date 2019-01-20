package com.polytech.xml.services;

public class MailTextItem extends MailItem{
	private String text;
	
	public MailTextItem(String text)
	{
		super("text");
		this.text=text;
	}

	public String getText() {
		return text;
	}
	
	
}
