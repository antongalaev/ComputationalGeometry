package mkm.gui;

import mkm.model.Edge;
import mkm.model.Face;
import mkm.model.Vertex;
import mkm.model.VertexType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.TreeSet;

public class MainFrame extends JFrame {

    private TreeSet<Vertex> queue = new TreeSet<>();
    private TreeSet<Edge> status = new TreeSet<>(Vertex.comp);
    private java.util.List<Vertex> points = new ArrayList<>();
    private java.util.List<Edge> segments = new ArrayList<>();
    private java.util.List<Edge> result = new ArrayList<>();
    private Vertex prev, next;
    private Vertex start;


    private JMenuBar anyBar = new JMenuBar();
    private JMenu anyMenu = new JMenu();
    private JMenuItem makeMenuItem = new JMenuItem();
    private JMenuItem trgMenuItem = new JMenuItem();
    private JMenuItem clearMenuItem = new JMenuItem();
    private JPanel anyPanel = new JPanel(){
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            if (points.size() == 0) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));
            for (Vertex p : points) {
                Ellipse2D e = new Ellipse2D.Double(p.getX() - 3, p.getY() - 3, 6, 6);
                g2.fill(e);
            }
            for (Edge s : segments) {
                Line2D l = new Line2D.Double(s.getUp().getX(), s.getUp().getY(),
                                             s.getDown().getX(), s.getDown().getY());
                g2.draw(l);
            }
            g2.setColor(Color.RED);
            for (Edge e : result) {
                Line2D l = new Line2D.Double(e.getUp().getX(), e.getUp().getY(),
                        e.getDown().getX(), e.getDown().getY());
                g2.draw(l);
            }
        }
    };

    public MainFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Make Monotone");
        setSize(400, 300);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = (int) toolkit.getScreenSize().getWidth();
        int screenHeight = (int) toolkit.getScreenSize().getHeight();
        setLocation((screenWidth-getWidth())/2, (screenHeight-getHeight())/2);
        anyMenu.setText("Menu");
        // Find menu item
        makeMenuItem.setText("Make monotone");
        makeMenuItem.setEnabled(false);
        makeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                makeMonotone();
                repaint();
            }
        });
        // Find menu item
        trgMenuItem.setText("Triangulate monotone");
        trgMenuItem.setEnabled(false);
        trgMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                triangulateMonotonePolygon(new Face(points));
                repaint();
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
                makeMenuItem.setEnabled(false);
                trgMenuItem.setEnabled(false);
                repaint();
            }
        });
        // add all to the frame
        anyMenu.add(makeMenuItem);
        anyMenu.add(trgMenuItem);
        anyMenu.add(new JSeparator(JSeparator.HORIZONTAL));
        anyMenu.add(clearMenuItem);
        anyBar.add(anyMenu);
        setJMenuBar(anyBar);
        anyPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (points.isEmpty()) {
                    prev = start = new Vertex(e.getX(), e.getY());
                    points.add(prev);
                } else if (Math.abs(e.getX() - start.getX()) < 5
                        && Math.abs(e.getY() - start.getY()) < 5) {
                    Edge edge = new Edge(prev, start);
                    prev.setEdge(edge);
                    prev.setNext(start);
                    start.setPrev(prev);
                    segments.add(edge);
                    makeMenuItem.setEnabled(true);
                    trgMenuItem.setEnabled(true);
                } else {
                    next = new Vertex(e.getX(), e.getY());
                    next.setPrev(prev);
                    prev.setNext(next);
                    points.add(next);
                    Edge edge = new Edge(prev, next);
                    prev.setEdge(edge);
                    segments.add(edge);
                    prev = next;
                }
                repaint();
            }
        });
        add(anyPanel);
        setVisible(true);
    }

    private void makeMonotone() {
        for (Vertex p : points) {
            queue.add(p);
        }
        result.clear();
        status.clear();
        while (! queue.isEmpty()) {
            Vertex next = queue.first();
            if (next.getNext().getY() > next.getY() && next.getPrev().getY() > next.getY()) {
                if (Vertex.direction(next.getPrev(), next, next.getNext()) < 0) {
                    next.setType(VertexType.START);
                    handleStartVertex(next);
                } else {
                    next.setType(VertexType.SPLIT);
                    handleSplitVertex(next);
                }
            } else if (next.getNext().getY() < next.getY() && next.getPrev().getY() < next.getY()) {
                if (Vertex.direction(next.getPrev(), next, next.getNext()) < 0) {
                    next.setType(VertexType.END);
                    handleEndVertex(next);
                } else {
                    next.setType(VertexType.MERGE);
                    handleMergeVertex(next);
                }
            } else {
                next.setType(VertexType.REGULAR);
                handleRegularVertex(next);
            }
            queue.remove(next);
        }
    }

    private void handleStartVertex(Vertex v) {
        status.add(v.getEdge());
        v.getEdge().setHelper(v);
    }

    private void handleEndVertex(Vertex v) {
        Edge edge = points.get(points.indexOf(v) - 1).getEdge();
        Vertex helper = edge.getHelper();
        if (helper.getType() == VertexType.MERGE) {
            result.add(new Edge(v, helper));
        }
        status.remove(edge);
    }

    private void handleSplitVertex(Vertex v) {
        Vertex.currY = v.getY();
        Edge e = status.lower(v.getEdge());
        result.add(new Edge(e.getHelper(), v));
        e.setHelper(v);
        status.add(v.getEdge());
        v.getEdge().setHelper(v);
    }

    private void handleMergeVertex(Vertex v) {
        // first part
        Edge edge = points.get(points.indexOf(v) - 1).getEdge();
        Vertex helper = edge.getHelper();
        if (helper.getType() == VertexType.MERGE) {
            result.add(new Edge(v, helper));
            status.remove(edge);
        }
        // second part
        Vertex.currY = v.getY();
        edge = status.lower(v.getEdge());
        helper = edge.getHelper();
        if (helper.getType() == VertexType.MERGE) {
            result.add(new Edge(v, helper));
        }
        edge.setHelper(v);
    }

    private void handleRegularVertex(Vertex v) {
        Vertex.currY = v.getY();
        if (v.getNext().getY() > v.getY()) {
            Edge edge = points.get(points.indexOf(v) - 1).getEdge();
            Vertex helper = edge.getHelper();
            if (helper.getType() == VertexType.MERGE) {
                result.add(new Edge(v, helper));
            }
            status.remove(edge);
            status.add(v.getEdge());
            v.getEdge().setHelper(v);
        } else {
            Edge edge = status.lower(v.getEdge());
            Vertex helper = edge.getHelper();
            if (helper.getType() == VertexType.MERGE) {
                result.add(new Edge(v, helper));
            }
            edge.setHelper(v);
        }
    }

    Stack<Vertex> s = new Stack<>();
    private void triangulateMonotonePolygon(Face face) {
        java.util.List<Vertex> list = face.getPoints();
        Collections.sort(list);
        Vertex start = list.get(0);
        s.push(start);
        s.push(list.get(1));
        for (int i = 2; i < list.size() - 1; i++) {
            int d1 = list.get(i).getPrev().getY() - list.get(i).getNext().getY();
            int d2 = s.peek().getPrev().getY() - s.peek().getNext().getY();
            if ((d1 < 0 && d2 > 0) || (d1 > 0 && d2 < 0)) {
                for (int j = 0; j < s.size() - 1; j ++) {
                    Vertex v = s.pop();
                    result.add(new Edge(list.get(i), v));
                }
                s.pop();
                s.push(list.get(i-1));
                s.push(list.get(i));
            } else {
                Vertex v = s.pop();
                while (! s.empty() && Vertex.direction(list.get(i), v, s.peek()) < 0) {
                    v = s.pop();
                    result.add(new Edge(list.get(i), v));
                }
                s.push(v);
                s.push(list.get(i));
            }
        }
        for (int j = 1; j < s.size() - 1; j ++) {
            Vertex v = s.get(j);
            result.add(new Edge(list.get(list.size()-1), v));
        }
        s.clear();
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
