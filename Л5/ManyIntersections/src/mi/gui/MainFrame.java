package mi.gui;

import mi.model.SegPoint;
import mi.model.Segment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.TreeSet;

public class MainFrame extends JFrame {

    private TreeSet<Segment> status = new TreeSet<>(SegPoint.comp);
    private TreeSet<SegPoint> queue = new TreeSet<>();
    private java.util.List<SegPoint> points = new ArrayList<>();
    private java.util.List<Segment> segments = new ArrayList<>();
    private java.util.List<SegPoint> result = new ArrayList<>();
    private SegPoint prev, next;
    private boolean fullSegment;

    private JMenuBar anyBar = new JMenuBar();
    private JMenu anyMenu = new JMenu();
    private JMenuItem checkMenuItem = new JMenuItem();
    private JMenuItem clearMenuItem = new JMenuItem();
    private JMenuItem infoMenuItem = new JMenuItem();
    private JPanel anyPanel = new JPanel(){
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            if (points.size() == 0) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));
            for (SegPoint p : points) {
                Ellipse2D e = new Ellipse2D.Double(p.getX() - 3, p.getY() - 3, 6, 6);
                g2.fill(e);
            }
            for (Segment s : segments) {
                Line2D l = new Line2D.Double(s.getUp().getX(), s.getUp().getY(),
                                             s.getDown().getX(), s.getDown().getY());
                g2.draw(l);
            }
            g2.setColor(Color.RED);
            for (SegPoint p : result) {
                Ellipse2D e = new Ellipse2D.Double(p.getX() - 3, p.getY() - 3, 6, 6);
                g2.fill(e);
            }
        }
    };

    public MainFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Many Intersections");
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
                findIntersections();
                infoMenuItem.setEnabled(true);
                repaint();
            }
        });
        infoMenuItem.setText("Show info");
        infoMenuItem.setEnabled(false);
        infoMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFrame infoFrame = new JFrame();
                JTextArea infoArea = new JTextArea();
                String info = "";
                for(SegPoint p : result) {
                    info += "At " + p + " intersect:\n";
                    for (Segment s : p.getC()) {
                        info += "\t" + s + "\n";
                    }
                    for (Segment s : p.getU()) {
                        info += "\t" + s + "\n";
                    }
                    for (Segment s : p.getL()) {
                        info += "\t" + s + "\n";
                    }
                }
                infoArea.setText(info);
                infoArea.setEditable(false);
                infoFrame.add(infoArea, BorderLayout.CENTER);
                infoFrame.setTitle("Info");
                infoFrame.setSize(400, 200);
                infoFrame.setLocation(100, 100);
                infoFrame.setVisible(true);
            }
        });
        // Clear menu item
        clearMenuItem.setText("Clear");
        clearMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                points.clear();
                queue.clear();
                status.clear();
                segments.clear();
                result.clear();
                fullSegment = false;
                checkMenuItem.setEnabled(false);
                infoMenuItem.setEnabled(false);
                repaint();
            }
        });
        // add all to the frame
        anyMenu.add(checkMenuItem);
        anyMenu.add(infoMenuItem);
        anyMenu.add(new JSeparator(JSeparator.HORIZONTAL));
        anyMenu.add(clearMenuItem);
        anyBar.add(anyMenu);
        setJMenuBar(anyBar);
        anyPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                next = new SegPoint(e.getX(), e.getY());
                if (fullSegment) {
                    boolean containsNext = points.contains(next);
                    boolean containsPrev = points.contains(prev);
                    if (containsNext) {
                        SegPoint cross = points.get(points.indexOf(next));
                        next = cross;
                    }
                    if (containsPrev) {
                        SegPoint cross = points.get(points.indexOf(prev));
                        prev = cross;
                    }
                    Segment s = new Segment(prev, next);
                    segments.add(s);
                    checkMenuItem.setEnabled(true);
                } else {
                    checkMenuItem.setEnabled(false);
                }
                if (! points.contains(next)) {
                    points.add(next);
                }
                prev = next;
                fullSegment = ! fullSegment;
                repaint();
            }
        });
        add(anyPanel);
        setVisible(true);
    }

    private void findIntersections() {
        for (SegPoint p : points) {
            queue.add(p);
        }
        result.clear();
        status.clear();
        while (! queue.isEmpty()) {
            SegPoint next = queue.first();
            handleEventPoint(next);
            queue.remove(next);
        }
    }

    private void handleEventPoint(SegPoint p) {
        TreeSet<Segment> U = p.getU();
        TreeSet<Segment> C = p.getC();
        TreeSet<Segment> L = p.getL();
        SegPoint.currY = p.getY();
        if (U.size() + C.size() + L.size() > 1) {
             result.add(p);
        }
        status.removeAll(L);
        status.removeAll(C);
        if (C.size() != 0)  SegPoint.currY ++;
        TreeSet<Segment> UC = new TreeSet<>(SegPoint.comp);
        for (Segment s : U) {
            status.add(s);
            UC.add(s);
        }
        for (Segment s : C) {
            status.add(s);
            UC.add(s);
        }
        if (UC.size() == 0) {
            Segment sl = status.lower(p.getL().first());
            Segment sr = status.higher(p.getL().last());
            findNewEvent(sl, sr, p);
        } else {
            Segment sl = UC.first();
            Segment sll = status.lower(sl);
            Segment sr = UC.last();
            Segment srr = status.higher(sr);
            findNewEvent(sl, sll, p);
            findNewEvent(sr, srr, p);
        }
    }

    private void findNewEvent(Segment sl, Segment sr, SegPoint p) {
        if (sl == null || sr == null) {
            return;
        }
        if (sl.intersects(sr)) {
            SegPoint cross = sl.countLineCross(sr);
            if (cross.compareTo(p) > 0) {
                if (queue.contains(cross)) {
                    cross = queue.ceiling(cross);
                    if (! cross.getC().contains(sl)) {
                        cross.appendC(sl);
                    }
                    if (! cross.getC().contains(sr)) {
                        cross.appendC(sr);
                    }
                }
                queue.add(cross);
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
