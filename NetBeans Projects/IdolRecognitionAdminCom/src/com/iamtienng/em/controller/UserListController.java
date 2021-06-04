/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.controller;

import com.iamtienng.em.model.User;
import com.iamtienng.em.service.UserService;
import com.iamtienng.em.service.UserServiceImpl;
import com.iamtienng.em.utility.UserTableModel;
import com.iamtienng.em.view.EditUserJFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author iamtienng
 */
public class UserListController {

    private JPanel jpnView;
    private JTextField jtfSearch;

    private UserService userService = null;

    private TableRowSorter<TableModel> rowSorter = null;

    private String[] listColumn = {"Serial", "Name", "Surname", "E-mail", "Account Type"};

    public UserListController(JPanel jpnView, JTextField jtfSearch) {
        this.jpnView = jpnView;
        this.jtfSearch = jtfSearch;

        this.userService = new UserServiceImpl();
    }

    public void setDateToTable() {
        List<User> listItem = userService.getList();
        DefaultTableModel model = new UserTableModel().setTableUser(listItem, listColumn);
        JTable table = new JTable(model);

        rowSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(rowSorter);

        jtfSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = jtfSearch.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = jtfSearch.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);

                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String text = jtfSearch.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);

                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        // Set size for Serial column
        table.getColumnModel().getColumn(0).setMinWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    int selectedRowIndex = table.getSelectedRow();
                    selectedRowIndex = table.convertRowIndexToModel(selectedRowIndex);

                    User user = new User();
                    user.setId((String) model.getValueAt(selectedRowIndex, 0));
                    user.setName((String) model.getValueAt(selectedRowIndex, 1));
                    user.setSurname((String) model.getValueAt(selectedRowIndex, 2));
                    user.setEmail((String) model.getValueAt(selectedRowIndex, 3));
                    user.setAccountType((String) model.getValueAt(selectedRowIndex, 4));

                    EditUserJFrame editUserJFrame = new EditUserJFrame(user);
                    editUserJFrame.setTitle("User Information");
                    editUserJFrame.setResizable(false);
                    editUserJFrame.setLocationRelativeTo(null);
                    editUserJFrame.setVisible(true);
                }
            }
        });

        table.getTableHeader().setFont(new Font("Montserrat", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(100, 50));
        table.setRowHeight(50);
        table.validate();
        table.repaint();

        JScrollPane scrollPane = new JScrollPane();

        scrollPane.getViewport().add(table);
        scrollPane.setPreferredSize(new Dimension(1300, 400));

        jpnView.removeAll();
        jpnView.setLayout(new BorderLayout());
        jpnView.add(scrollPane);
        jpnView.validate();
        jpnView.repaint();
    }

}
