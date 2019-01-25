package com.polytech.xml.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.xml.datatype.DatatypeConfigurationException;

import org.xml.sax.SAXException;

import com.polytech.xml.classes.MailThread;
import com.polytech.xml.services.MailerImpl;

public class InboxPanel extends JPanel{
	private TableModelMailbox tableModel;
	private MailerImpl mailer;
	
	private JPanel mailBoxPanel = new JPanel();
	private JPanel mailPanel= new JPanel();
	private JPanel replyPanel = new JPanel();
	
	private JLabel error = new JLabel();
	
	
	public InboxPanel(String user){
		mailer = new MailerImpl(user);
		tableModel = new TableModelMailbox(mailer.getListMailThread());
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		showMailBox();
		
	}	
	
	private void showMail(String fileName)
	{
		this.remove(mailPanel);
		mailPanel = new JPanel();
		MailThread mailThread = mailer.getMailThread(fileName);
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
				reply(fileName, mailThread);
			}
		});
		ShowMailPanel showMailPanel = new ShowMailPanel(fileName,mailThread, true);
		
		mailPanel.setLayout(new BoxLayout(mailPanel,BoxLayout.PAGE_AXIS));
		mailPanel.add(backButton);
		mailPanel.add(replyButton);
		mailPanel.add(showMailPanel);
		
		mailPanel.setVisible(true);
		mailBoxPanel.setVisible(false);
		replyPanel.setVisible(false);
		
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
	
	private void reply(String fileName, MailThread mail)
	{
		JButton send = new JButton("Envoyer");

		ShowMailPanel showMailPanel = new ShowMailPanel(fileName, mail, false);
		SendMailItemsPanel sendMailItemsPanel = new SendMailItemsPanel();
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					boolean isReplied = mailer.reply(fileName, showMailPanel.getHeader(), sendMailItemsPanel.getItemList(), showMailPanel.getValueList(), showMailPanel.getCheckedList(), showMailPanel.getSelectedRadioList());
					if (isReplied)
						back();
				}
				catch(SAXException | IOException ex)
				{
					error.setText(ex.toString());
					actualise();
				}
			}
		});
		replyPanel.add(showMailPanel);
		replyPanel.setVisible(true);
		mailPanel.setVisible(false);
		this.add(error);
		this.add(send);
		this.add(replyPanel);
		this.add(sendMailItemsPanel);
		this.revalidate();
		this.repaint();
	}
	
	private void actualise()
	{
		this.revalidate();
		this.repaint();
	}
	
	private void back()
	{
		replyPanel.setVisible(false);
		mailPanel.setVisible(false);
		mailBoxPanel.setVisible(true);
	}
}
