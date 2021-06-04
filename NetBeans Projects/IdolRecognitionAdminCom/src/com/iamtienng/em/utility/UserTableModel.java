/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.utility;

import com.iamtienng.em.model.User;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author iamtienng
 */
public class UserTableModel {

    public DefaultTableModel setTableUser(List<User> listItem, String[] listColumn) {
        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? Boolean.class : String.class;
            }
        };
        dtm.setColumnIdentifiers(listColumn);
        int columns = listColumn.length;
        Object[] obj = null;
        int rows = listItem.size();
        if (rows > 0) {
            for (int i = 0; i < rows; i++) {
                User user = listItem.get(i);
                obj = new Object[columns];
                obj[0] = user.getId();
                obj[1] = user.getName();
                obj[2] = user.getSurname();
                obj[3] = user.getEmail();
                obj[4] = user.getAccountType();

                dtm.addRow(obj);
            }
        }
        return dtm;
    }
}
