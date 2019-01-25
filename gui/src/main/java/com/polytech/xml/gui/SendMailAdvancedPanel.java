package com.polytech.xml.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.datatype.DatatypeConfigurationException;

import com.polytech.xml.classes.HeaderType;
import com.polytech.xml.services.ApplicationContext;
import com.polytech.xml.services.MailItem;
import com.polytech.xml.services.MailResponsesItem;
import com.polytech.xml.services.MailerImpl;
import com.polytech.xml.services.XSDService;
import com.polytech.xml.services.XSDType;

public class SendMailAdvancedPanel extends JPanel implements ActionListener{
	private JTextField recipient = new JTextField("recipient");
	private JTextArea object = new JTextArea(1,50);
	private JTextArea contentArea = new JTextArea(10,50);
	private JButton sendButton = new MyButton("SEND");
	private Boolean multipleValuesType = false;
	private Map<String,String> responseTypeMap = new HashMap<String,String>();
	private List<String> responseNameList = new ArrayList<String>();
	
	public SendMailAdvancedPanel(){	
		init();
		int caretPositionMin=0;
		int caretPositionMax=0;
		
		JLabel contentLabel = new JLabel("Contenu du mail : ");
		
		sendButton.addActionListener(this);
		sendButton.setActionCommand("send");
		
		contentArea.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				multipleValuesType=false;			
			}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		contentArea.addKeyListener(new KeyListener() {
			
			private String exp="";
			private Boolean auto = false;
			
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (multipleValuesType && e.getKeyCode()==10)
				{
					contentArea.insert("#option ", contentArea.getCaretPosition());
				}
				else if(e.getKeyChar()=='#')
				{
					auto=true;
				}
				else if (auto && Character.isAlphabetic(e.getKeyChar()))
				{
					exp+=e.getKeyChar();
					
					String itemName = "";
					int compteur=0;
					
					for (String item : responseNameList)
					{

						if(item.substring(0, exp.length()).equalsIgnoreCase(exp))
						{
							compteur++;
							itemName=item;
						}
					}
					
					if (compteur==1)
					{
						auto=false;
						contentArea.insert(itemName.substring(exp.length())+"\n", contentArea.getCaretPosition());
						if (itemName.equals("Choix") || itemName.equals("Selection"))
						{
							multipleValuesType=true;
							contentArea.insert("#option ", contentArea.getCaretPosition());
							int caret = contentArea.getCaretPosition();
							contentArea.insert("\n#End"+itemName+"\n", contentArea.getCaretPosition());
							contentArea.setCaretPosition(caret);
						}
						exp="";
						return;
					}
					else if (compteur==0)
					{
						auto=false;
						exp="";
					}
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
		this.add(contentArea);
		this.add(sendButton);
		
		//setSize(800,600); setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("send"))
		{
			try {
				send();
			} catch (DatatypeConfigurationException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	private void send() throws DatatypeConfigurationException
	{
		MailerImpl mailer = new MailerImpl(ApplicationContext.getUser());
		HeaderType header = new HeaderType();
		header.setRecipient(recipient.getText());
		header.setObject(object.getText());
		header.setSender(ApplicationContext.getUser());
		
		List<MailItem> items = new ArrayList<MailItem>();
		String content = contentArea.getText();
		BufferedReader br = new BufferedReader(new StringReader(content));
		
		try {
			HashMap<String,String> map = (HashMap<String, String>) getContentMap(br);
			System.out.println("LA");
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				System.out.println("Key : "+entry.getKey()+"\nValue : "+entry.getValue());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*char c;
		while ((c=(char) br.read())==-1)
		{
			if (c=='#')
		    {
		    	String typeName = stringToNextBlank(br);
		    	
		    	if (responseNameList.contains(typeName))
		    	{
		    		MailItem item = null;
		    		if(typeName.equals("choix") || typeName.equals("selection"))
		    		{
		    			List<String> optionList = new ArrayList<String>();
		    			
		    			while((c=(char) br.read())==-1 && c!='#');
		    			String optionType=stringToNextBlank(br);
		    			if(optionType.equals("option"))
		    			{
		    				
		    			}
		    			else if (optionType.equals("EndChoix") || optionType.equals("EndSelection"))
		    			{
		    				
		    			}
		    			else 
		    			{
		    				
		    			}

		    			item = new MailResponsesItem(type, values)		    		
		    		}
		    	}
		    }
		}
		for (int i = 0; i < content.length(); i++){
		    char c = content.charAt(i);     
		    int compteur=0;
		    
		}
		mailer.send(header, itemList)*/
	}

	private String stringToNextBlank(BufferedReader br) throws IOException
	{
		String s="";
		char c;
		while((c=(char) br.read())==-1 && (c!='\n' || c!=' ' || c!='\t'))
    		s+=c;
		return s;
	}
	
	private Map<String,String> getContentMap(BufferedReader br) throws IOException
	{
		//char c;
		while(br.read()!=-1)
		{
			System.out.println("FINi");
		}
		while(br.read()!=-1)
		{
			System.out.println("FINi2");
		}
		Map<String,String> map = new HashMap<String,String>();
		while(br.ready())
		{
			//System.out.println("RDY");
			String tag="";
			String text="";
			int i;
			while((i= br.read())!=-1 && ((char)i!='#'))
			{
				char c = (char)i;
				System.out.println("test");
				text+=c;
			}
			while((i= br.read())!=-1 && ((char)i!='\n' || (char)i!=' ' ||(char)i!='\t'))
			{
				char c = (char)i;
				System.out.println(tag);
				tag+=c;
			}
			map.put(tag, text);
		}

		return map;
	}
	
	private void init()
	{
		XSDService xsdService = new XSDService();
		ArrayList<XSDType> typeList = null;
		try {
			typeList=xsdService.getTypes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (XSDType type : typeList)
		{
			responseTypeMap.put(type.getName(), type.getId());
			responseNameList.add(type.getName());
		}
		System.out.println(responseNameList);
	}


}
