package com.polytech.xml.services;

import java.util.ArrayList;
import java.util.List;

public class MailResponsesItem extends MailItem{
	private ArrayList<String> values = null;
	
	public MailResponsesItem(String type, List<String> values)
	{
		super(type);
		this.values = (ArrayList<String>) values;
	}

	public ArrayList<String> getValues() {
		return values;
	}
}
