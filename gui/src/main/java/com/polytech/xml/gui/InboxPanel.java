package com.polytech.xml.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.polytech.xml.services.MailerImpl;

public class InboxPanel extends JPanel{
	private TableModelMailbox tableModel;
	private MailerImpl mailer;
	
	private JPanel mailBoxPanel = new JPanel();
	private JPanel mailPanel= new JPanel();
	
	public InboxPanel(String user){
		mailer = new MailerImpl(user);
		tableModel = new TableModelMailbox(mailer.getListMailThread());
		
		showMailBox();
		
	}	
	
	private void showMail(String fileName)
	{
		this.remove(mailPanel);
		mailPanel = new JPanel();
		JButton backButton = new JButton("Retour");
		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				back();
				
			}
		});
		JButton replyButton = new JButton("Répondre");
		replyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				reply();
				
			}
		});
		ShowMailPanel showMailPanel = new ShowMailPanel(fileName,mailer.getMailThread(fileName));
		
		mailPanel.setLayout(new BoxLayout(mailPanel,BoxLayout.PAGE_AXIS));
		mailPanel.add(backButton);
		mailPanel.add(replyButton);
		mailPanel.add(showMailPanel);
		
		mailPanel.setVisible(true);
		mailBoxPanel.setVisible(false);
		
		this.add(mailPanel);
		this.revalidate();
		this.repaint();
		
		
	}
	
	private void showMailBox()
	{
		JTable table = new JTable(tableModel);
		
		table.addMouseListener((new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
			    if (e.getClickCount() == 2) {
			      JTable target = (JTable)e.getSource();
			      int row = target.getSelectedRow();
			      
			      showMail(tableModel.getFileName(row));
			      
			      
			    }
			  }
			}));
		mailBoxPanel.add(table);
		this.add(mailBoxPanel);	
	}
	
	private void reply()
	{
		
	}
	
	private void back()
	{
		mailPanel.setVisible(false);
		mailBoxPanel.setVisible(true);
	}
}
