package cp.gui;

import cp.model.CpPoint;
import cp.model.PointPair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;

public class CpFrame extends JFrame {
    private ArrayList<CpPoint> points = new ArrayList<>();
    private PointPair closestPair;
    private JMenuBar cpBar = new JMenuBar();
    private JMenu cpMenu = new JMenu();
    private JMenuItem findMenuItem = new JMenuItem();
    private JMenuItem clearMenuItem = new JMenuItem();

    private JPanel cpPanel = new JPanel(){
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            if (points.size() == 0) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));
            for (CpPoint point : points) {
                Ellipse2D pt = new Ellipse2D.Double(point.getX()-3,point.getY()-3,6,6);
                g2.fill(pt);
            }
            if (closestPair == null) return;
            g2.setColor(Color.RED);
            CpPoint[] pair = closestPair.getPair();
            Ellipse2D pt = new Ellipse2D.Double(pair[0].getX()-3,pair[0].getY()-3,6,6);
            g2.fill(pt);
            pt = new Ellipse2D.Double(pair[1].getX()-3,pair[1].getY()-3,6,6);
            g2.fill(pt);
            Line2D line = new Line2D.Double(pair[0].getX(),pair[0].getY(),
                    pair[1].getX(),pair[1].getY());
            g2.draw(line);
        }
    };

    public CpFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Closest Pair");
        setSize(400, 300);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int) toolkit.getScreenSize().getWidth();
        int screenHeight = (int) toolkit.getScreenSize().getHeight();
        setLocation((screenWidth-getWidth())/2, (screenHeight-getHeight())/2);
        cpMenu.setText("Menu");
        // Find menu item
        findMenuItem.setText("Find");
        findMenuItem.setEnabled(false);
        findMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ArrayList<CpPoint> xlist, ylist;
                xlist = new ArrayList<>(); ylist = new ArrayList<>();
                xlist.addAll(points); ylist.addAll(points);
                Collections.sort(xlist, CpPoint.comparatorX);
                Collections.sort(ylist, CpPoint.comparatorY);
                closestPair = closestPair(xlist, ylist);
                repaint();
            }
        });
        // Clear menu item
        clearMenuItem.setText("Clear");
        clearMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                points.clear();
                findMenuItem.setEnabled(false);
                closestPair = null;
                repaint();
            }
        });
        // add all to the frame
        cpMenu.add(findMenuItem);
        cpMenu.add(new JSeparator(JSeparator.HORIZONTAL));
        cpMenu.add(clearMenuItem);
        cpBar.add(cpMenu);
        setJMenuBar(cpBar);
        cpPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                points.add(new CpPoint(e.getX(), e.getY()));
                if (points.size() > 1) {
                    findMenuItem.setEnabled(true);
                }
                repaint();
            }
        });
        add(cpPanel);
        setVisible(true);
    }

    private PointPair closestPair(ArrayList<CpPoint> x, ArrayList<CpPoint> y) {
        PointPair min;
        int size = x.size();
        if (size < 4) { // end of recursion
            if (size == 2) {
                return new PointPair(x.get(0), x.get(1));
            }
            if (size == 3) { // brute force
                min = new PointPair(x.get(1), x.get(2));
                for (int i = 1; i < size; i ++) {
                    if (PointPair.countDistance(x.get(0), x.get(i)) < min.getDistance()) {
                        min = new PointPair(x.get(0), x.get(i));
                    }
                }
                return min;
            }
        }
        // dividing arrays
        ArrayList<CpPoint> xl, xr, yl, yr, ys;
        xl = new ArrayList<>(); xr = new ArrayList<>();
        yl = new ArrayList<>(); yr = new ArrayList<>();
        int m = (size + 1) / 2;
        for (int i = 0; i < m; ++ i) {
            xl.add(x.get(i));
        }
        for (int i = m; i < size; ++ i) {
            xr.add(x.get(i));
        }
        int xm = x.get(m).getX();
        for (CpPoint point : y) {
            if (point.getX() < xm) {
                yl.add(point);
            } else {
                yr.add(point);
            }
        }
        PointPair left, right;
        left = closestPair(xl, yl);
        right = closestPair(xr, yr);
        min = left;
        if (right.getDistance() < left.getDistance()) {
            min = right;
        }
        ys = new ArrayList<>();
        for(CpPoint point : y) {
            if (Math.abs(xm - point.getX()) < min.getDistance()) {
                ys.add(point);
            }
        }
        PointPair closest = min;
        for (int i = 0; i < ys.size(); ++ i) {
            int k = i + 1;
            while (k < ys.size() &&
                    ys.get(k).getY() - ys.get(i).getY() < min.getDistance()) {
                if (PointPair.countDistance(ys.get(k), ys.get(i)) < closest.getDistance()) {
                    closest = new PointPair(ys.get(k), ys.get(i));
                }
                ++ k;
            }
        }
        return closest;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CpFrame();
            }
        });
    }
}





