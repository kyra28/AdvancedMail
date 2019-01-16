package com.polytech.xml.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.polytech.xml.services.MailerImpl;

public class MailBoxFrame extends JFrame{

	public static String user;

	
	private JPanel inboxPanel;
	private JPanel sendMailPanel = new SendMailPanel();
	private JPanel sendMailAdvancedPanel = new SendMailAdvancedPanel();
	private JPanel activePanel;
	
	private JPanel centerPanel = new JPanel();
	
	public MailBoxFrame(String user){
		super("MailBox de "+user);
		this.user=user;
		inboxPanel = new InboxPanel(user);
		activePanel=inboxPanel;
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		
		JPanel menuPanel = new JPanel();
		
		JButton inboxButton = new MyButton("Reception");
		inboxButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				activePanel.setVisible(false);
				inboxPanel.setVisible(true);		
				activePanel=inboxPanel;
			}
		});
		
		JButton writeButton = new MyButton("Ecrire");
		writeButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				activePanel.setVisible(false);
				sendMailPanel.setVisible(true);
				activePanel=sendMailPanel;
			}
		});
		
		JButton writeAdvancedButton = new MyButton("Ecrire Avancé");
		writeAdvancedButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				activePanel.setVisible(false);
				sendMailAdvancedPanel.setVisible(true);
				System.out.println("ok");
				activePanel=sendMailAdvancedPanel;
			}
		});
		
		JButton disconectButton = new MyButton("Deconnexion");
		disconectButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		menuPanel.add(inboxButton);
		menuPanel.add(writeButton);
		menuPanel.add(writeAdvancedButton);
		menuPanel.add(disconectButton);
		
		contentPane.add(menuPanel,BorderLayout.NORTH);
		
		
		
		
		centerPanel.add(inboxPanel);
		centerPanel.add(sendMailPanel);
		centerPanel.add(sendMailAdvancedPanel);
		contentPane.add(centerPanel,BorderLayout.CENTER);
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setSize(800,600); setVisible(true);	sendMailPanel.setVisible(false);
		
	}
	
	
}
