package ls.gui;

/** @author Anton Galaev */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LSFrame extends JFrame {
    private LSPanel lsPanel = new LSPanel();
    private JMenuBar lsBar = new JMenuBar();
    private JMenu lsMenu = new JMenu();
    private JMenuItem lsMenuItem = new javax.swing.JMenuItem();


    public LSFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Line Segments Intersection");
        setSize(400, 300);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int) toolkit.getScreenSize().getWidth();
        int screenHeight = (int) toolkit.getScreenSize().getHeight();
        setLocation((screenWidth-getWidth())/2, (screenHeight-getHeight())/2);
        lsMenu.setText("Functions");
        lsMenuItem.setText("Solve");
        lsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                boolean res = lsPanel.segmentsIntersect();
                if (res) {
                    JOptionPane.showMessageDialog(lsPanel,"Line segments intersect",
                            "Result",JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(lsPanel,"Line segments do not intersect",
                            "Result",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        lsMenu.add(lsMenuItem);
        lsBar.add(lsMenu);
        setJMenuBar(lsBar);
        add(lsPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        LSFrame frame = new LSFrame();
    }
}





