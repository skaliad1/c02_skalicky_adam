package controller;

import model.Line;
import model.Polygon;
import rasterizer.LineRasterizer;
import view.Panel;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller2D {
    private Panel panel;
    private LineRasterizer rasterizer;

    private List<Line> lines;
    private Polygon polygon;


    private boolean isLineMode = true;


    private int startX, startY;
    private int dragX, dragY;
    private boolean isDragging = false;


    private int hoverX, hoverY;
    private final int Toleration = 10;

    public Controller2D(Panel panel) {
        this.panel = panel;
        this.rasterizer = new LineRasterizer(panel);

        this.lines = new ArrayList<>();
        this.polygon = new Polygon();

        initListeners();
    }

    private void initListeners() {
        panel.setFocusable(true);
        panel.requestFocus();


        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                dragX = e.getX();
                dragY = e.getY();
                isDragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isLineMode) {
                    lines.add(new Line(startX, startY, e.getX(), e.getY(), 0xFFFFFF));
                } else {
                    polygon.addPoint(e.getX(), e.getY());
                }
                isDragging = false;
                redrawScene();
            }
        });


        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                dragX = e.getX();
                dragY = e.getY();
                redrawScene();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

                hoverX = e.getX();
                hoverY = e.getY();
            }
        });


        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    lines.clear();
                    polygon.clear();
                } else if (e.getKeyCode() == KeyEvent.VK_L) {
                    isLineMode = true;
                } else if (e.getKeyCode() == KeyEvent.VK_P) {
                    isLineMode = false;
                } else if (e.getKeyCode() == KeyEvent.VK_X) {

                    deletePointAt(hoverX, hoverY);
                }
                redrawScene();
            }
        });
    }


    private void deletePointAt(int x, int y) {

        List<Point> points = polygon.getPoints();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (Math.hypot(p.x - x, p.y - y) <= Toleration) {
                points.remove(i);
                return;
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            Line l = lines.get(i);
            if (Math.hypot(l.getX1() - x, l.getY1() - y) <= Toleration ||
                    Math.hypot(l.getX2() - x, l.getY2() - y) <= Toleration) {
                lines.remove(i);
                return;
            }
        }
    }

    private void redrawScene() {
        panel.clear();

        for (Line line : lines) {
            rasterizer.rasterize(line.getX1(), line.getY1(), line.getX2(), line.getY2(), line.getColor());
        }

        List<Point> points = polygon.getPoints();
        int size = points.size();

        if (size > 1) {
            for (int i = 0; i < size - 1; i++) {
                rasterizer.rasterize(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, 0xFFFFFF);
            }
        }

        if (isDragging) {
            if (isLineMode) {
                rasterizer.rasterize(startX, startY, dragX, dragY, 0xFF0000);
            } else {
                if (size >= 1) rasterizer.rasterize(points.get(size - 1).x, points.get(size - 1).y, dragX, dragY, 0xFF0000);
                if (size >= 2) rasterizer.rasterize(dragX, dragY, points.get(0).x, points.get(0).y, 0xFF0000);
            }
        }

        if (size >= 3 && !isDragging) {
            rasterizer.rasterize(points.get(size - 1).x, points.get(size - 1).y, points.get(0).x, points.get(0).y, 0xFFFFFF);
        }

        panel.repaint();
    }
}