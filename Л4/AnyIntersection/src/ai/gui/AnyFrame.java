package ai.gui;

import ai.model.AnyPoint;
import ai.model.Segment;
import ai.model.Side;
import ai.model.SweepStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class AnyFrame extends JFrame {

    private ArrayList<AnyPoint> schedule = new ArrayList<AnyPoint>();
    private SweepStatus status = new SweepStatus();
    private ArrayList<Segment> segments = new ArrayList<Segment>();
    private boolean cross;

    private JMenuBar anyBar = new JMenuBar();
    private JMenu anyMenu = new JMenu();
    private JMenuItem checkMenuItem = new JMenuItem();
    private JMenuItem clearMenuItem = new JMenuItem();
    private JPanel anyPanel = new JPanel(){
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            if (schedule.size() == 0) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));
            for (AnyPoint p : schedule) {
                g2.drawOval(p.getX() - 3, p.getY() - 3, 6, 6);
            }
            for (Segment s : segments) {
                g2.drawLine(s.getLeft().getX(), s.getLeft().getY(),
                            s.getRight().getX(), s.getRight().getY());
            }
        }
    };

    public AnyFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Any Intersection");
        setSize(400, 300);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int) toolkit.getScreenSize().getWidth();
        int screenHeight = (int) toolkit.getScreenSize().getHeight();
        setLocation((screenWidth-getWidth())/2, (screenHeight-getHeight())/2);
        anyMenu.setText("Menu");
        // Find menu item
        checkMenuItem.setText("Check");
        checkMenuItem.setEnabled(false);
        checkMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                boolean result = AnyIntersection();
                if (result) {
                    JOptionPane.showMessageDialog(anyPanel, "An Intersection Found",
                            "Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(anyPanel, "No Intersections Found",
                            "Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        // Clear menu item
        clearMenuItem.setText("Clear");
        clearMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                schedule.clear();
                segments.clear();
                checkMenuItem.setEnabled(false);
                cross = false;
                repaint();
            }
        });
        // add all to the frame
        anyMenu.add(checkMenuItem);
        anyMenu.add(new JSeparator(JSeparator.HORIZONTAL));
        anyMenu.add(clearMenuItem);
        anyBar.add(anyMenu);
        setJMenuBar(anyBar);
        anyPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AnyPoint next = new AnyPoint(e.getX(), e.getY());
                if (schedule.contains(next)) cross = true;
                schedule.add(next);
                if (schedule.size() % 2 == 0) {
                    segments.add(new Segment(schedule.get(schedule.size() - 1), schedule.get(schedule.size() - 2)));
                    checkMenuItem.setEnabled(true);
                } else {
                    checkMenuItem.setEnabled(false);
                }
                repaint();
            }
        });
        add(anyPanel);
        setVisible(true);
    }

    private boolean AnyIntersection() {
        if (cross) return true;
        status.clear();
        Collections.sort(schedule);
        for (AnyPoint p : schedule) {
            Segment s = p.getSegment();
            if (p.getSide() == Side.LEFT) {
                status.insert(s);
                Segment above = status.above(s);
                Segment below = status.below(s);
                if (above != null && above.intersects(s) ||
                    below != null && below.intersects(s)) {
                    return true;
                }
            } else {
                Segment above = status.above(s);
                Segment below = status.below(s);
                if (above != null && below != null &&
                        above.intersects(below)) {
                    return true;
                }
                status.delete(s);
            }
        }
        return false;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AnyFrame();
            }
        });
    }
}
