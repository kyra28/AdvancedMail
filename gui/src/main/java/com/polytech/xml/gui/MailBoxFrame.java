package com.polytech.xml.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.polytech.xml.services.MailerImpl;

public class MailBoxFrame extends JFrame{

	private String user;
	private TableModelMailbox tableModel;
	public MailBoxFrame(String user){
		super("MailBox de "+user);
		this.user=user;
		
		
		MailerImpl mailer = new MailerImpl(user);
		
		JPanel boite = new JPanel();
		
		tableModel = new TableModelMailbox(mailer.getListEchange());
		JTable table = new JTable(tableModel);
		
		table.addMouseListener((new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
			    if (e.getClickCount() == 2) {
			      JTable target = (JTable)e.getSource();
			      int row = target.getSelectedRow();
			      int column = target.getSelectedColumn();
			      
			      System.out.println(tableModel.getValueAt(row, column));
			    }
			  }
			}));
		
		boite.add(table);
		
		setContentPane(boite);
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setSize(800,600); setVisible(true);	
		
	}
	
	
}
