/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.controller;

import com.iamtienng.em.service.UserService;
import com.iamtienng.em.service.UserServiceImpl;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

/**
 *
 * @author iamtienng
 */
public class ChangePasswordController {

    private JPasswordField jpfOldPassword;
    private JPasswordField jpfNewPassword;
    private JPasswordField jpfConfirmPassword;
    private JButton btnChangePassword;
    private JLabel jlbMsg;

    private UserService userService;

    public ChangePasswordController(JPasswordField jpfOldPassword, JPasswordField jpfNewPassword, JPasswordField jpfConfirmPassword, JButton btnChangePassword, JLabel jlbMsg) {
        this.jpfOldPassword = jpfOldPassword;
        this.jpfNewPassword = jpfNewPassword;
        this.jpfConfirmPassword = jpfConfirmPassword;
        this.btnChangePassword = btnChangePassword;
        this.jlbMsg = jlbMsg;

        this.userService = new UserServiceImpl();
    }

    public void setEvent() {
        btnChangePassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (String.valueOf(jpfOldPassword.getPassword()).length() == 0 || String.valueOf(jpfNewPassword.getPassword()).length() == 0 || String.valueOf(jpfConfirmPassword.getPassword()).length() == 0) {
                    jlbMsg.setText("Please fill the mandatory data.");
                } else if (String.valueOf(jpfOldPassword.getPassword()).length() < 6) {
                    jlbMsg.setText("Please check your current Password.");
                } else if (String.valueOf(jpfNewPassword.getPassword()).length() < 6) {
                    jlbMsg.setText("New Password must have 6-18 characters.");
                } else if (!String.valueOf(jpfNewPassword.getPassword()).equals(String.valueOf(jpfConfirmPassword.getPassword()))) {
                    jlbMsg.setText("Please confirm Password.");
                } else {
                    Boolean checkPassword = userService.changePassword(UserAuthenticationController.tokenGlobal, String.valueOf(jpfOldPassword.getPassword()), String.valueOf(jpfNewPassword.getPassword()));

                    System.out.println(checkPassword.toString());
                    if (checkPassword) {
                        jlbMsg.setText("Success change Password.");
                    } else {
                        jlbMsg.setText("Please check the info again.");
                    }
                }
            }
        });
    }

}
