package com.polytech.xml.gui;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.polytech.xml.classes.HeaderType;
import com.polytech.xml.classes.MailThread;

public class TableModelMailbox extends AbstractTableModel{
	private final ArrayList<HeaderType> headers = new ArrayList<HeaderType>();
	private final ArrayList<String> fileNameList = new ArrayList<String>();
	private final String[] entetes = {"Objet","Expediteur","date"};
	
	public TableModelMailbox(Map<String,MailThread> threadMap) {
		for (Map.Entry<String, MailThread> entry : threadMap.entrySet())
		{
			headers.add(entry.getValue().getMail().getHeader());
			fileNameList.add(entry.getKey());
		}
	}
	
	public String getFileName(int rowIndex)
	{
		return fileNameList.get(rowIndex);
	}
	
	public int getRowCount() {
		return headers.size();
	}

	public int getColumnCount() {
		return entetes.length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
        case 0:
            return headers.get(rowIndex).getObject();
        case 1:
            return headers.get(rowIndex).getSender();
        case 2:
        	return headers.get(rowIndex).getDate();
        default:
            return null; //Ne devrait jamais arriver
    }
	}

}
