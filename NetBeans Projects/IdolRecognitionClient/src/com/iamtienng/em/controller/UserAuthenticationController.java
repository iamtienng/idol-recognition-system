/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.controller;

import com.iamtienng.em.view.MainJFrame;
import com.iamtienng.em.view.UserCreateNewUserJFrame;
import com.iamtienng.em.service.UserService;
import com.iamtienng.em.service.UserServiceImpl;
import java.awt.Dialog;
import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public class UserAuthenticationController {

    public static String tokenGlobal = "";
    public static String nameGlobal = "";
    public static String surnameGlobal = "";
    public static String emailGlobal = "";
    public static String accountTypeGlobal = "";

    private Dialog dialog;
    private JButton btnRegister;
    private JButton btnLogin;
    private JTextField jtfEmail;
    private JPasswordField jpfPassword;
    private JLabel jlbMsg;

    private UserService userService;

    public UserAuthenticationController(Dialog dialog, JButton btnRegister, JButton btnLogin, JTextField jtfEmail, JPasswordField jpfPassword, JLabel jlbMsg) {
        this.dialog = dialog;
        this.btnRegister = btnRegister;
        this.btnLogin = btnLogin;
        this.jtfEmail = jtfEmail;
        this.jpfPassword = jpfPassword;
        this.jlbMsg = jlbMsg;

        this.userService = new UserServiceImpl();
    }

    public void setEvent() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    if (jtfEmail.getText().length() == 0
                            || String.valueOf(jpfPassword.getPassword()).length() == 0) {
                        jlbMsg.setText("Please fill the mandatory data.");
                    } else if (isValid(jtfEmail.getText()) == false) {
                        jlbMsg.setText("Please enter the valid email.");
                    } else if (String.valueOf(jpfPassword.getPassword()).length() == 0) {
                        jlbMsg.setText("Please check your Password.");
                    } else {
                        JSONObject userInfo = new JSONObject();
                        Boolean checkUser = userService.checkUser(jtfEmail.getText().toLowerCase(), String.valueOf(jpfPassword.getPassword()));

                        if (checkUser) {
                            userInfo = userService.getUserInfo(jtfEmail.getText().toLowerCase(), String.valueOf(jpfPassword.getPassword()));
                        } else {
                            jlbMsg.setText("Please check your E-mail or Password.");
                        }
                        if (userInfo != null) {
                            nameGlobal = userInfo.getString("name");
                            surnameGlobal = userInfo.getString("surname");
                            emailGlobal = userInfo.getString("email");
                            accountTypeGlobal = userInfo.getString("accountType").toUpperCase();
                            tokenGlobal = userInfo.getString("token");

                            dialog.dispose();
                            MainJFrame frame = new MainJFrame();
                            frame.setLocationRelativeTo(null);
                            frame.setVisible(true);
                        }

                    }
                } catch (JSONException ex) {
                    System.out.println(ex.toString());
                }
            }
        });
        btnRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
                UserCreateNewUserJFrame frame = new UserCreateNewUserJFrame();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        jtfEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
                }
            }
        });
        jpfPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
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

    public static String md5(String string) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(string.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
