package com.polytech.xml.services;

public class XSDType {
	private String id;
	private String name;
	private String type;
	
	public XSDType(String id, String name)
	{
		this.id=id;
		this.name=name;
		if (id.equals("choiceType") || id.equals("selectorType"))
			type = "multiple";
		else
			type = "single";
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
}
