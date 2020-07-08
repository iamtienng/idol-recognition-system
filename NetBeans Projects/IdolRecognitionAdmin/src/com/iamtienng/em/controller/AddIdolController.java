package com.iamtienng.em.controller;

import com.iamtienng.em.service.IdolService;
import com.iamtienng.em.service.IdolServiceImpl;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author iamtienng
 */
public class AddIdolController {

    private JLabel jlbIdolName;
    private JTextField jtfIdolName;
    private JButton btnAddNewIdol;

    private IdolService idolService;

    public AddIdolController(JLabel jlbIdolName, JTextField jtfIdolName, JButton btnAddNewIdol) {
        this.jlbIdolName = jlbIdolName;
        this.jtfIdolName = jtfIdolName;
        this.btnAddNewIdol = btnAddNewIdol;

        this.idolService = new IdolServiceImpl();
    }

    public void setEvent() {
        btnAddNewIdol.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Boolean addingStatus = idolService.addIdol(String.valueOf(jtfIdolName.getText()));
            }

        });

    }

}
