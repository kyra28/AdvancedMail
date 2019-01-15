package com.polytech.xml.gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.polytech.xml.classes.HeaderType;
import com.polytech.xml.classes.MailThread;

public class TableModelMailbox extends AbstractTableModel{
	private final ArrayList<HeaderType> headers = new ArrayList<HeaderType>();
	private final String[] entetes = {"Objet","Expediteur","date"};
	
	public TableModelMailbox(ArrayList<MailThread> threads) {
		for (MailThread thread : threads)
		{
			headers.add(thread.getMail().getHeader());
		}
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
