package com.iamtienng.em.controller;

import com.iamtienng.em.service.IdolService;
import com.iamtienng.em.service.IdolServiceImpl;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public class AddIdolController {

    private JLabel jlbIdolName;
    private JTextField jtfIdolName;
    private JButton btnAddNewIdol;
    private JLabel jLabelNTruePositive;
    private JLabel jLabelNFalsePositive;
    private JLabel jLabelNFalseNegative;
    private JLabel jLabelNTrueNegative;
    private JLabel jLabelTotal;
    private JLabel jLabelSensitivity;
    private JLabel jLabelPrecision;
    private JLabel jLabelAccuracy;

    private IdolService idolService;

    public AddIdolController(JLabel jlbIdolName, JTextField jtfIdolName, JButton btnAddNewIdol, JLabel jLabelNTruePositive, JLabel jLabelNFalsePositive, JLabel jLabelNFalseNegative, JLabel jLabelNTrueNegative, JLabel jLabelTotal, JLabel jLabelSensitivity, JLabel jLabelPrecision, JLabel jLabelAccuracy) {
        this.jlbIdolName = jlbIdolName;
        this.jtfIdolName = jtfIdolName;
        this.btnAddNewIdol = btnAddNewIdol;
        this.jLabelNTruePositive = jLabelNTruePositive;
        this.jLabelNFalsePositive = jLabelNFalsePositive;
        this.jLabelNFalseNegative = jLabelNFalseNegative;
        this.jLabelNTrueNegative = jLabelNTrueNegative;
        this.jLabelTotal = jLabelTotal;
        this.jLabelSensitivity = jLabelSensitivity;
        this.jLabelPrecision = jLabelPrecision;
        this.jLabelAccuracy = jLabelAccuracy;

        this.idolService = new IdolServiceImpl();
    }

    public void setEvent() {
        btnAddNewIdol.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Boolean addingStatus = idolService.addIdol(String.valueOf(jtfIdolName.getText()));
            }

        });
        JSONObject confusionMatrix = idolService.getMatrix();

        int TP = confusionMatrix.getInt("nTruePositive");
        int FP = confusionMatrix.getInt("nFalsePositive");
        int FN = confusionMatrix.getInt("nFalseNegative");
        int TN = confusionMatrix.getInt("nTrueNegative");
        int total = TP + FP + FN + TN;
        jLabelTotal.setText("" + total);
        jLabelNTruePositive.setText("" + TP);
        jLabelNFalsePositive.setText("" + FP);
        jLabelNFalseNegative.setText("" + FN);
        jLabelNTrueNegative.setText("" + TN);
        float SEN = (float) Math.round(((float) TP / (TP + FN)) * 10000) / 100;
        float PRE = (float) Math.round(((float) TP / (TP + FP)) * 10000) / 100;
        float ACC = (float) Math.round(((float) (TP + TN) / (TP + FP + TN + FN)) * 10000) / 100;
        jLabelSensitivity.setText(SEN + "%");
        jLabelPrecision.setText(PRE + "%");
        jLabelAccuracy.setText(ACC + "%");
    }

}
