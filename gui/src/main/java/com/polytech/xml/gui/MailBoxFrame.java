package com.polytech.xml.gui;

import java.awt.Color;

import javax.swing.JFrame;

public class MailBoxFrame extends JFrame{

	private String user;
	public MailBoxFrame(String user){
		super("MailBox de "+user);
		this.user=user;
		
		//setContentPane(contentPane);
		
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setSize(800,600); setVisible(true);	
		
	}
	
	
}
