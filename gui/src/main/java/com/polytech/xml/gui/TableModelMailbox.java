package com.polytech.xml.gui;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.polytech.xml.classes.Echange;
import com.polytech.xml.classes.HeaderType;

public class TableModelMailbox extends AbstractTableModel{
	private final ArrayList<HeaderType> headers = new ArrayList<HeaderType>();
	private final String[] entetes = {"Objet","Expediteur","date"};
	
	public TableModelMailbox(ArrayList<Echange> echanges) {
		for (Echange echange : echanges)
		{
			headers.add(echange.getMail().get(0).getHeader());
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
            return headers.get(rowIndex).getObjet();
        case 1:
            return headers.get(rowIndex).getExpediteur();
        case 2:
        	return headers.get(rowIndex).getDate();
        default:
            return null; //Ne devrait jamais arriver
    }
	}

}
