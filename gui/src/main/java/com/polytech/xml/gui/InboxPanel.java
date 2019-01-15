package com.polytech.xml.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;

import com.polytech.xml.services.MailerImpl;

public class InboxPanel extends JPanel{
	private TableModelMailbox tableModel;
	
	public InboxPanel(String user){
		MailerImpl mailer = new MailerImpl(user);
		
		tableModel = new TableModelMailbox(mailer.getListMailThread());
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
		
		this.add(table);	
	}	
	
}
