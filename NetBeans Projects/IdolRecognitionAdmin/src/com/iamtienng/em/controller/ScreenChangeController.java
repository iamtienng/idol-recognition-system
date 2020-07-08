package com.iamtienng.em.controller;

import com.iamtienng.em.bean.BeanList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.iamtienng.em.view.AddIdolJPanel;
import com.iamtienng.em.view.ListIdolsJPanel;
import com.iamtienng.em.view.ListUsersJPanel;
import com.iamtienng.em.view.UserAuthenticationJDialog;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author iamtienng
 */
public class ScreenChangeController {

    private JFrame jFrame;
    private JButton btnLogOut;
    private JPanel root;
    private String kindSelected;

    private List<BeanList> listItem = null;

//    public ScreenChangeController(JPanel root) {
//        this.root = root;
//    }
    public ScreenChangeController(JFrame jFrame, JButton btnLogOut, JPanel root) {
        this.jFrame = jFrame;
        this.btnLogOut = btnLogOut;
        this.root = root;
    }

    public void setView(JPanel jpnItem, JLabel jlbItem) {
        kindSelected = "History";
        jpnItem.setBackground(new Color(15, 31, 35));
        jlbItem.setBackground(new Color(15, 31, 35));

        root.removeAll();
        root.setLayout(new BorderLayout());
        root.add(new AddIdolJPanel());
        root.invalidate();
        root.repaint();
    }

    public void setEvent(List<BeanList> listItem) {
        this.listItem = listItem;
        for (BeanList item : listItem) {
            item.getJlb().addMouseListener(new LabelEvent(item.getKind(), item.getJpn(), item.getJlb()));
        }
    }

    class LabelEvent implements MouseListener {

        private JPanel node;

        private String kind;
        private JPanel jpnItem;
        private JLabel jlbItem;

        public LabelEvent(String kind, JPanel jpnItem, JLabel jlbItem) {
            this.kind = kind;
            this.jpnItem = jpnItem;
            this.jlbItem = jlbItem;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            switch (kind) {
                case "History":
                    node = new AddIdolJPanel();
                    break;
                case "Profile":
                    node = new ListUsersJPanel();
                    break;
                case "IdolRecognitor":
                    node = new ListIdolsJPanel();
                    break;
                default:
                    node = new AddIdolJPanel();
                    break;
            }
            root.removeAll();
            root.setLayout(new BorderLayout());
            root.add(node);
            root.validate();
            root.repaint();
            setChangeBackground(kind);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            kindSelected = kind;
            jpnItem.setBackground(new Color(15, 31, 35));
            jlbItem.setBackground(new Color(15, 31, 35));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            jpnItem.setBackground(new Color(15, 31, 35));
            jlbItem.setBackground(new Color(15, 31, 35));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!kindSelected.equalsIgnoreCase(kind)) {
                jpnItem.setBackground(new Color(110, 102, 88));
                jlbItem.setBackground(new Color(110, 102, 88));
            }
        }

    }

    private void setChangeBackground(String kind) {
        for (BeanList item : listItem) {
            if (item.getKind().equalsIgnoreCase(kind)) {
                item.getJpn().setBackground(new Color(15, 31, 35));
                item.getJlb().setBackground(new Color(15, 31, 35));
            } else {
                item.getJpn().setBackground(new Color(110, 102, 88));
                item.getJlb().setBackground(new Color(110, 102, 88));
            }
        }
    }

    public void setLogOut() {
        btnLogOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jFrame.dispose();
                UserAuthenticationJDialog dialog = new UserAuthenticationJDialog(null, true);
                dialog.setTitle("Idol Recognitor");
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

            }
        });
    }
}
