package com.iamtienng.em.controller;

import static com.iamtienng.em.controller.UserAuthenticationController.tokenGlobal;
import com.iamtienng.em.service.IdolService;
import com.iamtienng.em.service.IdolServiceImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.json.JSONObject;

public class IdolRecognitorController {

    private JPanel jpnView;
    private JButton btnAdd;
    private JTextField jtfLink;
    private JLabel jlbMsg;

    private IdolService idolService;

    public IdolRecognitorController(JPanel jpnView, JButton btnAdd, JTextField jtfLink, JLabel jlbMsg) {
        this.jpnView = jpnView;
        this.btnAdd = btnAdd;
        this.jtfLink = jtfLink;
        this.jlbMsg = jlbMsg;

        idolService = new IdolServiceImpl();
    }

    public void setEvent() {
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JSONObject idolJSONObject = new JSONObject();
                if (!isValid(jtfLink.getText())) {
                    jlbMsg.setText("Please enter a valid Link URL!");
                } else {
                    idolJSONObject = idolService.findIdol(tokenGlobal, jtfLink.getText());
                    if (idolJSONObject != null) {
                        jlbMsg.setText(idolJSONObject.getJSONObject("idol").getString("name"));
                    }
                }

            }
        });

        jtfLink.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnAdd.doClick();
                }
            }
        });
    }

    public static boolean isValid(String url) {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        } // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }
}
