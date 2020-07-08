/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.controller;

import com.iamtienng.em.model.Idol;
import com.iamtienng.em.service.IdolService;
import com.iamtienng.em.service.IdolServiceImpl;
import com.iamtienng.em.utility.IdolTableModel;
import com.iamtienng.em.view.EditIdolJFrame;
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
public class IdolListController {

    private JPanel jpnView;

    private JTextField jtfSearch;

    private IdolService idolService = null;

    private TableRowSorter<TableModel> rowSorter = null;

    private String[] listColumn = {"ID", "Name", "User DATA", "PersonID"};

    public IdolListController(JPanel jpnView, JTextField jtfSearch) {
        this.jpnView = jpnView;

        this.jtfSearch = jtfSearch;

        this.idolService = new IdolServiceImpl();
    }

    public void setDateToTable() {
        List<Idol> listItem = idolService.getList();
//        for (Idol idoltemp : listItem) {
//            System.out.println(idoltemp.getId());
//            System.out.println(idoltemp.getName());
//        }
        DefaultTableModel model = new IdolTableModel().setTableUser(listItem, listColumn);
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

                    Idol idol = new Idol();
                    idol.setId((String) model.getValueAt(selectedRowIndex, 0));
                    idol.setName((String) model.getValueAt(selectedRowIndex, 1));
                    idol.setUserData((String) model.getValueAt(selectedRowIndex, 2));
                    idol.setPersonId((String) model.getValueAt(selectedRowIndex, 3));

                    EditIdolJFrame editIdolJFrame = new EditIdolJFrame(idol);
                    editIdolJFrame.setTitle("Idol Information");
                    editIdolJFrame.setResizable(false);
                    editIdolJFrame.setLocationRelativeTo(null);
                    editIdolJFrame.setVisible(true);
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
