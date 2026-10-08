package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private List<Point> points;

    public Polygon() {
        points = new ArrayList<>();
    }

    public void addPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getSize() {
        return points.size();
    }

    public void clear() {
        points.clear();
    }
}