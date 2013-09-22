package ih.gui;

import ih.model.ClickState;
import ih.model.ConvexRegion;
import ih.model.Halfplane;
import ih.model.Point;

import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainFrame extends JFrame {

    private List<Halfplane> halfplanes = new LinkedList<>();
    private ConvexRegion result = new ConvexRegion(new ArrayList<Point>());
    private JMenuBar bar = new JMenuBar();
    private JMenu menu = new JMenu();
    private JMenuItem checkMenuItem = new JMenuItem();
    private JMenuItem clearMenuItem = new JMenuItem();
    private JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));
            double x1 = 0.0;
            double x2 = getWidth();
            for (Halfplane h : halfplanes) {
                Point2D p1 = new Point2D.Double(x1, h.y(x1)), p2 = new Point2D.Double(x2, h.y(x2));
                Line2D line = new Line2D.Double(p1, p2);
                g2.draw(line);
                for (int x = 0; x < getWidth(); x +=10) {
                    Point2D s1 = new Point2D.Double(x, h.y(x));
                    Point2D s2;
                    if (h.isBelow()) {
                        s2 = new Point2D.Double(x, h.y(x) + 3);
                    } else {
                        s2 = new Point2D.Double(x, h.y(x) - 3);
                    }
                    Line2D shortLine = new Line2D.Double(s1, s2);
                    g2.draw(shortLine);
                }
            }
            g2.setColor(Color.RED);
            for (Point p : result.getPoints(new ConvexRegion())) {
                Ellipse2D el = new Ellipse2D.Double(p.getX(), p.getY(), 5, 5);
                g2.draw(el);
            }
        }
    };

    public MainFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Halfplanes intersection");
        setSize(400, 300);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int) toolkit.getScreenSize().getWidth();
        int screenHeight = (int) toolkit.getScreenSize().getHeight();
        setLocation((screenWidth-getWidth())/2, (screenHeight-getHeight())/2);
        menu.setText("menu");
        // Find menu item
        checkMenuItem.setText("Check");
        checkMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                result = intersectHalfplanes(halfplanes);
                repaint();
            }
        });
        // Clear menu item
        clearMenuItem.setText("Clear");
        clearMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                halfplanes.clear();
                result = new ConvexRegion(new ArrayList<Point>());
                repaint();
            }
        });
        // add all to the frame
        menu.add(checkMenuItem);
        menu.add(new JSeparator(JSeparator.HORIZONTAL));
        menu.add(clearMenuItem);
        bar.add(menu);
        setJMenuBar(bar);
        panel.addMouseListener(new MouseAdapter() {
            ClickState state = ClickState.FIRST;
            Point first = null;
            Point second = null;
            @Override
            public void mouseClicked(MouseEvent e) {
                Point curr = new Point(e.getPoint());
                if (state == ClickState.THIRD) {
                    halfplanes.add(new Halfplane(first, second, curr));
                    state = ClickState.FIRST;
                } else if (state == ClickState.FIRST) {
                    first = curr;
                    state = ClickState.SECOND;
                } else {
                    second = curr;
                    state = ClickState.THIRD;
                }
                repaint();
            }
        });
        add(panel);
    }

    private ConvexRegion intersectHalfplanes(List<Halfplane> halfplanes) {
        int n = halfplanes.size();
        if (n == 1) {
            return halfplanes.get(0);
        } else {
            List<Halfplane> h1 = new LinkedList<>();
            List<Halfplane> h2 = new LinkedList<>();
            for (int i = 0; i < n; ++ i) {
                if (i < n / 2) {
                    h1.add(halfplanes.get(i));
                } else {
                    h2.add(halfplanes.get(i));
                }
            }
            ConvexRegion c1 = intersectHalfplanes(h1);
            ConvexRegion c2 = intersectHalfplanes(h2);
            return intersectConvexRegions(c1, c2);
        }
    }

    private ConvexRegion intersectConvexRegions(ConvexRegion c1, ConvexRegion c2) {
        List<Point> intersection = new LinkedList<>();
        List<Point> p1 = c1.getPoints(c2);
        List<Point> p2 = c2.getPoints(c1);
        for (Point p : p1) {
            if (c2.containsPoint(p)) {
                intersection.add(p);
            }
        }
        for (Point p : p2) {
            if (c1.containsPoint(p)) {
                intersection.add(p);
            }
        }
        return new ConvexRegion(intersection);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
