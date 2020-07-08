/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.controller;

import com.iamtienng.em.model.User;
import com.iamtienng.em.service.UserService;
import com.iamtienng.em.service.UserServiceImpl;
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
public class EditUserController {

    private JFrame jFrameEditUserFrame;
    private JButton btnSubmit;
    private JButton btnDelete;
    private JLabel jLabelId;
    private JTextField jtfName;
    private JTextField jtfSurname;
    private JTextField jtfAccountType;
    private JTextField jtfEmail;
    private JLabel jLabelMessage;

    private User user;

    private UserService userService;

    public EditUserController(JFrame jFrameEditUserFrame, JButton btnSubmit, JButton btnDelete, JLabel jLabelId, JTextField jtfName, JTextField jtfSurname, JTextField jtfAccountType, JTextField jtfEmail, JLabel jLabelMessage) {
        this.jFrameEditUserFrame = jFrameEditUserFrame;
        this.btnSubmit = btnSubmit;
        this.btnDelete = btnDelete;
        this.jLabelId = jLabelId;
        this.jtfName = jtfName;
        this.jtfSurname = jtfSurname;
        this.jtfAccountType = jtfAccountType;
        this.jtfEmail = jtfEmail;
        this.jLabelMessage = jLabelMessage;

        this.userService = new UserServiceImpl();
    }

    public void setView(User user) {
        this.user = user;

        jLabelId.setText(user.getId());
        jtfName.setText(user.getName());
        jtfSurname.setText(user.getSurname());
        jtfEmail.setText(user.getEmail());
        jtfAccountType.setText(user.getAccountType());
        setEventButtonDelete();
        setEventButtonSubmit();
    }

    public void setEventButtonSubmit() {
        btnSubmit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jtfName.getText().length() == 0 || jtfSurname.getText().length() == 0 || jtfEmail.getText().length() == 0 || jtfAccountType.getText().length() == 0) {
                    jLabelMessage.setText("Please fill the Idol Name!");
                } else {
                    user.setName(jtfName.getText());
                    user.setSurname(jtfSurname.getText());
                    user.setEmail(jtfEmail.getText());
                    user.setAccountType(jtfAccountType.getText());

                    String newData;
                    newData = userService.updateUser(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getAccountType());
                    if (newData != null) {
                        jLabelMessage.setText("Successfully updated!");
                    }
                }
            }
        });
    }

    public void setEventButtonDelete() {
        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (userService.deleteUser(user.getId())) {
                    jFrameEditUserFrame.dispose();
                } else {
                    jLabelMessage.setText("Fail to delete idol!");
                }
            }
        });
    }

}
