package com.polytech.xml.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyButton extends JButton{

	public MyButton(String text){
		super(text);
		this.setPreferredSize(new Dimension(120, 30));
	}

	
	
}
