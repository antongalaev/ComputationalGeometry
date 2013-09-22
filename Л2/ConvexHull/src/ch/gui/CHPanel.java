package ch.gui;

import ch.model.CHPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

class CHPanel extends JPanel {
    ArrayList<CHPoint> list;
    ArrayList<CHPoint> jarvis;
    Stack<CHPoint> stack;
    Object lock = new Object();
    boolean hull = false;

    public CHPanel() {
        super();
        list = new ArrayList<CHPoint>();
        jarvis = new ArrayList<CHPoint>();
        stack = new Stack<CHPoint>();
    }

    public void grahamScan() {
        synchronized (lock) {
            hull = true;
            stack.clear();
            min();
            for(int i = 0; i < 3; i ++) {
                stack.push(list.get(i));
            }
            for (int i = 3; i < list.size(); ++ i){
                CHPoint next = list.get(i);
                while (CHPoint.direction(underTop(),top(),next) <= 0) {
                    stack.pop();
                }
                stack.push(next);
                try {
                    Thread.sleep(500);
                    repaint();
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stack.add(stack.get(0));
            repaint();
        }
    }

    public void jarvisMatch() {
        synchronized (lock) {
            hull = true;
            stack.clear();
            jarvis.clear();
            CHPoint min = min();
            jarvis.addAll(list);
            stack.push(jarvis.get(0));
            CHPoint next = jarvis.get(1);
            stack.push(next);
            jarvis.remove(next);
            while (next != min) {
                next = findNext(next);
                stack.push(next);
                jarvis.remove(next);
                try {
                    Thread.sleep(500);
                    repaint();
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private CHPoint min() {
        CHPoint min = list.get(0);
        int index = 0;
        for (int i = 1; i < list.size(); ++ i) {
            if (list.get(i).getY() < min.getY()) {
                min = list.get(i);
                index = i;
            }
        }
        list.remove(index);
        CHPoint.setMin(min);
        Collections.sort(list);
        list.add(0, min);
        return min;
    }

    private CHPoint findNext(CHPoint curr) {
        CHPoint next = jarvis.get(0);
        for (CHPoint p : jarvis) {
            int product = CHPoint.direction(curr,next,p);
            if (product < 0) {
                next = p;
            }
        }
        return next;
    }

    public void  paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        synchronized (lock) {
            if (list.size() == 0) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(3));
            for (CHPoint point : list) {
                Ellipse2D pt = new Ellipse2D.Double(point.getX()-3,point.getY()-3,6,6);
                g2.draw(pt);g2.fill(pt);
                g2.drawString(String.valueOf(list.indexOf(point)+1),point.getX()+3,point.getY()-3);
            }
            if (hull) {
                for (int i = 0; i < stack.size() - 1; ++ i) {
                    CHPoint point1 = stack.get(i);
                    CHPoint point2 = stack.get(i + 1);
                    Line2D line = new Line2D.Double(point1.getX(),point1.getY(),
                            point2.getX(),point2.getY());
                    g2.draw(line);
                }
            }
            lock.notifyAll();
        }

    }

    public int addPoint(int x, int y) {
        list.add(new CHPoint(x,y));
        return list.size();
    }

    public void removePoints() {
        list.clear();
        stack.clear();
        hull = false;
    }

    private CHPoint top() {
        return stack.peek();
    }

    private CHPoint underTop() {
        return stack.get(stack.size() - 2);
    }
}