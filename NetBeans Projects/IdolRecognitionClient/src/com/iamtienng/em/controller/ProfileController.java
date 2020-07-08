package com.iamtienng.em.controller;

import com.iamtienng.em.view.ChangePasswordJFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author iamtienng
 */
public class ProfileController {

    private JLabel jlbName;
    private JLabel jlbSurname;
    private JLabel jlbEmail;
    private JLabel jlbAccountType;
    private JButton btnChangePassword;

    public ProfileController(JLabel jlbName, JLabel jlbSurname, JLabel jlbEmail, JLabel jlbAccountType, JButton btnChangePassword) {
        this.jlbName = jlbName;
        this.jlbSurname = jlbSurname;
        this.jlbEmail = jlbEmail;
        this.jlbAccountType = jlbAccountType;
        this.btnChangePassword = btnChangePassword;
    }

    public void setEvent() {
        jlbName.setText(UserAuthenticationController.nameGlobal);
        jlbSurname.setText(UserAuthenticationController.surnameGlobal);
        jlbEmail.setText(UserAuthenticationController.emailGlobal);
        jlbAccountType.setText(UserAuthenticationController.accountTypeGlobal);
    }

    public void setChangePassword() {
        btnChangePassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ChangePasswordJFrame frame = new ChangePasswordJFrame();
                frame.setTitle("Change Password");
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
            }
        });
    }

}
