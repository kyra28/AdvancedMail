package com.polytech.xml.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MultipleValuesTypePanel extends JPanel implements ActionListener{
	private JPanel valuePanel = new JPanel();
	private ArrayList<JTextArea> textAreaList = new ArrayList<JTextArea>();


	public MultipleValuesTypePanel(){
		JButton plus = new JButton("+");
		plus.setActionCommand("addValue");
		plus.addActionListener(this);
		valuePanel.setLayout(new BoxLayout(valuePanel,BoxLayout.PAGE_AXIS));
		JTextArea textArea1 = new JTextArea(1,50);
		JTextArea textArea2 = new JTextArea(1,50);
		textAreaList.add(textArea1);
		textAreaList.add(textArea2);
		valuePanel.add(textArea1);
		valuePanel.add(textArea2);
		this.add(valuePanel);
		this.add(plus);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addValue"))
		{
			JPanel value = new JPanel();
			JTextArea textArea = new JTextArea(1,50);
			textAreaList.add(textArea);
			
			JButton delete = new JButton("x");
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					valuePanel.remove(value);
					textAreaList.remove(textArea);
					valuePanel.revalidate();
					valuePanel.repaint();
				}
			});
			
			value.add(delete);
			value.add(textArea);
			valuePanel.add(value);
			valuePanel.revalidate();
		}
		
	}
	public List<String> getValueList() {
		ArrayList<String> valueList = new ArrayList<String>();
		for (JTextArea textArea : textAreaList)
		{
			valueList.add(textArea.getText());
		}
		return valueList;
	}
}
