/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.controller;

import com.iamtienng.em.model.Idol;
import com.iamtienng.em.service.IdolService;
import com.iamtienng.em.service.IdolServiceImpl;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author iamtienng
 */
public class EditIdolController {

    private JFrame jFrameEditIdolFrame;
    private JButton btnSubmit;
    private JButton btnDelete;
    private JLabel jLabelId;
    private JTextField jtfNameIdol;
    private JLabel jLabelPersonId;
    private JLabel jLabelUserData;
    private JLabel jLabelMessage;

    private Idol idol = null;

    private IdolService idolService = null;

    public EditIdolController(JFrame jFrameEditIdolFrame, JButton btnSubmit, JButton btnDelete, JLabel jLabelId, JTextField jtfNameIdol, JLabel jLabelPersonId, JLabel jLabelUserData, JLabel jLabelMessage) {
        this.jFrameEditIdolFrame = jFrameEditIdolFrame;
        this.btnSubmit = btnSubmit;
        this.btnDelete = btnDelete;
        this.jLabelId = jLabelId;
        this.jtfNameIdol = jtfNameIdol;
        this.jLabelPersonId = jLabelPersonId;
        this.jLabelUserData = jLabelUserData;
        this.jLabelMessage = jLabelMessage;

        this.idolService = new IdolServiceImpl();
    }

    public void setView(Idol idol) {
        this.idol = idol;
        //set data to view
        jLabelId.setText(idol.getId());
        jtfNameIdol.setText(idol.getName());
        jLabelPersonId.setText(idol.getPersonId());
        jLabelUserData.setText(idol.getUserData());
        setEventButtonSubmit();
        setEventButtonDelete();
    }

    public void setEventButtonSubmit() {
        btnSubmit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jtfNameIdol.getText().length() == 0) {
                    jLabelMessage.setText("Please fill the Idol Name!");
                } else {
                    idol.setName(jtfNameIdol.getText());
                    String newName;
                    newName = idolService.updateIdol(idol.getId(), idol.getName());
                    if (newName != null) {
                        jLabelMessage.setText("Changed name to " + newName + "!");
                    }
                }
            }
        });
    }

    public void setEventButtonDelete() {
        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (idolService.deleteIdol(idol.getId())) {
                    jFrameEditIdolFrame.dispose();
                } else {
                    jLabelMessage.setText("Fail to delete idol!");
                }
            }
        });
    }

}
