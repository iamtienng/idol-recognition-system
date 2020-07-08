/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.controller;

import com.iamtienng.em.service.AdminService;
import com.iamtienng.em.service.AdminServiceImpl;
import com.iamtienng.em.view.UserAuthenticationJDialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author iamtienng
 */
public class UserCreateNewUserController {

    private JFrame registerJFrame;
    private JTextField jtfName;
    private JTextField jtfSurname;
    private JTextField jtfEmail;
    private JPasswordField jpfPassword;
    private JPasswordField jpfPasswordConfirm;
    private JButton jbtSubmit;
    private JLabel jlbMsg;

    private AdminService adminService;

    public UserCreateNewUserController(JFrame registerJFrame, JTextField jtfName, JTextField jtfSurname, JTextField jtfEmail, JPasswordField jpfPassword, JPasswordField jpfPasswordConfirm, JButton jbtSubmit, JLabel jlbMsg) {
        this.registerJFrame = registerJFrame;
        this.jtfName = jtfName;
        this.jtfSurname = jtfSurname;
        this.jtfEmail = jtfEmail;
        this.jpfPassword = jpfPassword;
        this.jpfPasswordConfirm = jpfPasswordConfirm;
        this.jbtSubmit = jbtSubmit;
        this.jlbMsg = jlbMsg;

        adminService = new AdminServiceImpl();
    }

    public void setEvent() {
        registerJFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                registerJFrame.dispose();
                UserAuthenticationJDialog dialog = new UserAuthenticationJDialog(null, true);
                dialog.setTitle("Idol Recognitor");
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });

        jbtSubmit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (jtfName.getText().length() == 0 || jtfSurname.getText().length() == 0 || jtfEmail.getText().length() == 0
                            || String.valueOf(jpfPassword.getPassword()).length() == 0 || String.valueOf(jpfPasswordConfirm.getPassword()).length() == 0) {
                        jlbMsg.setText("Please fill the mandatory data.");
                    } else if (isValid(jtfEmail.getText()) == false) {
                        jlbMsg.setText("Please enter a valid email.");
                    } else if (String.valueOf(jpfPassword.getPassword()).length() < 6 && String.valueOf(jpfPassword.getPassword()).length() < 18) {
                        jlbMsg.setText("Please use Password must have 6-18 characters.");
                    } else if (!String.valueOf(jpfPassword.getPassword()).equals(String.valueOf(jpfPasswordConfirm.getPassword()))) {
                        jlbMsg.setText("Please confirm Password.");
                    } else {
                        Boolean checkUser = adminService.createAdmin(jtfName.getText(), jtfSurname.getText(), jtfEmail.getText().toLowerCase(), String.valueOf(jpfPassword.getPassword()));

                        if (checkUser) {
                            jlbMsg.setText("Success!");
                            registerJFrame.dispose();
                            UserAuthenticationJDialog dialog = new UserAuthenticationJDialog(null, true);
                            dialog.setTitle("Idol Recognitor");
                            dialog.setResizable(false);
                            dialog.setLocationRelativeTo(null);
                            dialog.setVisible(true);
                        } else {
                            jlbMsg.setText("Fail!");
                        }

                    }
                } catch (Exception ex) {
                    jlbMsg.setText(ex.toString());
                }
            }
        });
    }

    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }
}
