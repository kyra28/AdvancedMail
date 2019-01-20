package com.polytech.xml.services;

public abstract class MailItem {
	private String type;
	
	public MailItem(String type)
	{
		this.type=type;
	}

	public String getType() {
		return type;
	}
	
}
