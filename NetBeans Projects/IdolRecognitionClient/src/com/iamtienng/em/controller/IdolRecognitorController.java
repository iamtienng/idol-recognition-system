package com.iamtienng.em.controller;

import static com.iamtienng.em.controller.UserAuthenticationController.tokenGlobal;
import com.iamtienng.em.service.IdolService;
import com.iamtienng.em.service.IdolServiceImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.json.JSONObject;

public class IdolRecognitorController {

    private JPanel jpnView;
    private JButton btnAdd;
    private JTextField jtfLink;
    private JLabel jlbMsg;
    private JPanel jPanelVote;
    private JRadioButton jRBTruePositive;
    private JRadioButton jRBFalsePositive;
    private JRadioButton jRBFalseNegative;
    private JRadioButton jRBTrueNegative;
    private JButton jButtonSubmit;
    private JLabel jLabelMessage;
    private ButtonGroup confusionMatrix;

    private IdolService idolService;

    public IdolRecognitorController(JPanel jpnView, JButton btnAdd, JTextField jtfLink, JLabel jlbMsg, JPanel jPanelVote, JRadioButton jRBTruePositive, JRadioButton jRBFalsePositive, JRadioButton jRBFalseNegative, JRadioButton jRBTrueNegative, JButton jButtonSubmit, JLabel jLabelMessage, ButtonGroup confusionMatrix) {
        this.jpnView = jpnView;
        this.btnAdd = btnAdd;
        this.jtfLink = jtfLink;
        this.jlbMsg = jlbMsg;
        this.jPanelVote = jPanelVote;
        this.jRBTruePositive = jRBTruePositive;
        this.jRBFalsePositive = jRBFalsePositive;
        this.jRBFalseNegative = jRBFalseNegative;
        this.jRBTrueNegative = jRBTrueNegative;
        this.jButtonSubmit = jButtonSubmit;
        this.jLabelMessage = jLabelMessage;
        this.confusionMatrix = confusionMatrix;

        idolService = new IdolServiceImpl();
    }
    private String idHistory;

    public void setEvent() {

        jPanelVote.setVisible(false);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JSONObject idolJSONObject = new JSONObject();
                if (!isValid(jtfLink.getText())) {
                    jlbMsg.setText("Please enter a valid Link URL!");
                } else {
                    idolJSONObject = idolService.findIdol(tokenGlobal, jtfLink.getText());
                    if (idolJSONObject != null) {
                        idHistory = (String) idolJSONObject.get("idHistory");
//                        System.out.println(idHistory);
                        jlbMsg.setText(idolJSONObject.getJSONObject("idol").getString("name"));
                        jPanelVote.setVisible(true);
                    }
                }

            }
        });

        jButtonSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jRBTruePositive.isSelected() || jRBFalsePositive.isSelected() || jRBFalseNegative.isSelected() || jRBTrueNegative.isSelected()) {

                    idolService.submitMatrix(idHistory, jRBTruePositive.isSelected(), jRBFalsePositive.isSelected(), jRBFalseNegative.isSelected(), jRBTrueNegative.isSelected());
                    confusionMatrix.clearSelection();
                    jPanelVote.setVisible(false);
                } else {
                    jLabelMessage.setText("Please choose one option!");
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
