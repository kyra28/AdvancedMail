package com.polytech.xml.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConnexionForm extends JFrame implements ActionListener{

	public ConnexionForm(){
		JPanel contentPane = new JPanel();
		JLabel label = new JLabel("Quel utilisateur voulez-vous utiliser?");
		JButton user1 = new JButton("user1");
		user1.addActionListener(this);
		JButton user2 = new JButton("user2");
		user2.addActionListener(this);
		
		
		contentPane.add(label);
		contentPane.add(user1);
		contentPane.add(user2);
		
		setContentPane(contentPane);
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setSize(800,600); setVisible(true);		
	}

	public void actionPerformed(ActionEvent e) {
		new MailBoxFrame(((JButton)e.getSource()).getText());
		this.setVisible(false);
	}
	
	
}
