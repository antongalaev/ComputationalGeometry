package ch.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CHFrame extends JFrame {
    private CHPanel chPanel = new CHPanel();
    private JMenuBar chBar = new JMenuBar();
    private JMenu chMenu = new JMenu();
    private JMenuItem grahamMenuItem = new JMenuItem();
    private JMenuItem jarvisMenuItem = new JMenuItem();
    private JMenuItem clearMenuItem = new JMenuItem();

    public CHFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Convex Hull");
        setSize(400, 300);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int) toolkit.getScreenSize().getWidth();
        int screenHeight = (int) toolkit.getScreenSize().getHeight();
        setLocation((screenWidth-getWidth())/2, (screenHeight-getHeight())/2);
        chMenu.setText("Functions");
        // Graham's scan menu item
        grahamMenuItem.setText("Graham's scan");
        grahamMenuItem.setEnabled(false);
        grahamMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new Thread(){
                    public void run(){
                        chPanel.grahamScan();
                    }
                }.start();
            }
        });
        // Jarvis's match menu item
        jarvisMenuItem.setText("Jarvis's match");
        jarvisMenuItem.setEnabled(false);
        jarvisMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new Thread(){
                    public void run(){
                        chPanel.jarvisMatch();
                    }
                }.start();
            }
        });
        // Clear menu item
        clearMenuItem.setText("Clear");
        clearMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chPanel.removePoints();
                grahamMenuItem.setEnabled(false);
                jarvisMenuItem.setEnabled(false);
                chPanel.repaint();
            }
        });
        // add all to the frame
        chMenu.add(grahamMenuItem);
        chMenu.add(jarvisMenuItem);
        chMenu.add(new JSeparator(JSeparator.HORIZONTAL));
        chMenu.add(clearMenuItem);
        chBar.add(chMenu);
        setJMenuBar(chBar);
        chPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chPanelMouseClicked(e);
            }
        });
        add(chPanel);
        setVisible(true);
    }

    private void chPanelMouseClicked(MouseEvent e) {
        if (chPanel.addPoint(e.getX(), e.getY()) > 2){
            grahamMenuItem.setEnabled(true);
            jarvisMenuItem.setEnabled(true);
        }
        chPanel.repaint();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CHFrame();
            }
        });
    }
}





