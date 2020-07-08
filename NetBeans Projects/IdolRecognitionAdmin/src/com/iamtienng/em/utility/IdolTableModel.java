/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.utility;

import com.iamtienng.em.model.Idol;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author iamtienng
 */
public class IdolTableModel {

    public DefaultTableModel setTableUser(List<Idol> listItem, String[] listColumn) {
        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 4 ? Boolean.class : String.class;
            }
        };
        dtm.setColumnIdentifiers(listColumn);
        int columns = listColumn.length;
        Object[] obj = null;
        int rows = listItem.size();
        if (rows > 0) {
            for (int i = 0; i < rows; i++) {
                Idol idol = listItem.get(i);
                obj = new Object[columns];
                obj[0] = idol.getId();
                obj[1] = idol.getName();
                obj[2] = idol.getUserData();
                obj[3] = idol.getPersonId();

                dtm.addRow(obj);
            }
        }
        return dtm;
    }
}
