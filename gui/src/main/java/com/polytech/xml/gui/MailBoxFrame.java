package com.polytech.xml.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.polytech.xml.services.MailerImpl;

public class MailBoxFrame extends JFrame{

	private String user;
	public MailBoxFrame(String user){
		super("MailBox de "+user);
		this.user=user;
		
		
		MailerImpl mailer = new MailerImpl(user);
		
		JPanel boite = new JPanel();
		
		TableModelMailbox tableModel = new TableModelMailbox(mailer.getListEchange());
		JTable table = new JTable(tableModel);
		
		boite.add(table);
		
		setContentPane(boite);
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setSize(800,600); setVisible(true);	
		
	}
	
	
}
