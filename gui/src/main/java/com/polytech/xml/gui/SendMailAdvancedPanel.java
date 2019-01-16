package com.polytech.xml.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.datatype.DatatypeConfigurationException;

public class SendMailAdvancedPanel extends JPanel implements ActionListener{
	private JTextField recipient = new JTextField("recipient");
	private JTextArea object = new JTextArea(1,50);
	private JTextArea content = new JTextArea(10,50);
	private JButton sendButton = new MyButton("SEND");
	private Boolean multipleValuesType = false;
	public SendMailAdvancedPanel(){		
		int caretPositionMin=0;
		int caretPositionMax=0;
		
		JLabel contentLabel = new JLabel("Contenu du mail : ");
		
		sendButton.addActionListener(this);
		sendButton.setActionCommand("send");
		
		content.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				multipleValuesType=false;			
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		content.addKeyListener(new KeyListener() {
			private String[] responseTypeArray = {"Libre","Numeric","Date","Telephone","Choix","Selection"};
			private String exp="";
			private Boolean auto = false;
			
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println(e.getKeyCode());
				if (multipleValuesType && e.getKeyCode()==10)
				{
					content.insert("#option ", content.getCaretPosition());
				}
				else if(e.getKeyChar()=='#')
				{
					auto=true;
				}
				else if (auto && Character.isAlphabetic(e.getKeyChar()))
				{
					exp+=e.getKeyChar();
					for (String item : responseTypeArray)
					{
						if(item.substring(0, exp.length()).equalsIgnoreCase(exp))
						{
							auto=false;
							content.insert(item.substring(exp.length())+"\n", content.getCaretPosition());
							if (item.equals("Choix") || item.equals("Selection"))
							{
								multipleValuesType=true;
								content.insert("#option ", content.getCaretPosition());
								int caret = content.getCaretPosition();
								content.insert("\n#End"+item+"\n", content.getCaretPosition());
								content.setCaretPosition(caret);
							}
							exp="";
							return;
						}
					}
					auto=false;
					exp="";
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {

				
			}
		});
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
		this.add(recipient);
		this.add(object);
		this.add(contentLabel);
		this.add(content);
		this.add(sendButton);
		
		//setSize(800,600); setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("send"))
		{
			try {
				send();
			} catch (DatatypeConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	private void send() throws DatatypeConfigurationException
	{
		
	}



}
