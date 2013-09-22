package ls.gui;

/** @author Anton Galaev */

import ls.model.LSPoint;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

class LSPanel extends JPanel {
    LSPoint[] points;
    int current;

    public LSPanel() {
        super();
        points = new LSPoint[4];
        current = 0;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lsPanelMouseClicked(e);
            }
        });

    }

    public boolean segmentsIntersect() {
        if (points[3] == null) {
            return false;
        }
        LSPoint p1 = points[0],
                p2 = points[1],
                p3 = points[2],
                p4 = points[3];
        int d1 = LSPoint.direction(p3, p4, p1);
        int d2 = LSPoint.direction(p3, p4, p2);
        int d3 = LSPoint.direction(p1, p2, p3);
        int d4 = LSPoint.direction(p1, p2, p4);
        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
                ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))){
            return true;
        }
        else if (d1 == 0 && LSPoint.onSegment(p3, p4, p1)) {
            return true;
        }
        else if (d2 == 0 && LSPoint.onSegment(p3, p4, p2)) {
            return true;
        }
        else if (d3 == 0 && LSPoint.onSegment(p1, p2, p3)){
            return true;
        }
        else if (d4 == 0 && LSPoint.onSegment(p1, p2, p4)){
            return true;
        }
        return false;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(3));
        for (int i = 0; i < 4; i ++) {
            if (points[i] != null) {
                Ellipse2D pt = new Ellipse2D.Double(points[i].getX()-3,points[i].getY()-3,6,6);
                g2.draw(pt);
                g2.fill(pt);
                g2.drawString(String.valueOf(i+1),points[i].getX()+3,points[i].getY()-3);
            }
        }
        for (int i = 1; i < 4; i += 2) {
            if (points[i] != null) {
                Line2D line = new Line2D.Double(points[i-1].getX(),points[i-1].getY(),
                        points[i].getX(),points[i].getY());
                g2.draw(line);
            }
        }
    }

    private void lsPanelMouseClicked(MouseEvent e) {
        points[current++] = new LSPoint(e.getX(), e.getY());
        if (current == 4) {
            current = 0;
        }
        repaint();
    }
}