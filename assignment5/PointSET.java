import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdIn;

import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private final Set<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        //
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Set<Point2D> inside = new TreeSet<>();
        for (Point2D p : points) {
            boolean isInWidth = p.x() >= rect.xmin() && p.x() <= rect.xmax();
            boolean isInHeight = p.y() >= rect.ymin() && p.y() <= rect.ymax();
            if (isInHeight && isInWidth) inside.add(p);
        }
        return inside;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double min = 2;
        Point2D minPoint = null;
        for (Point2D point : points) {
            double distance = point.distanceSquaredTo(p);
            if (distance < min) {
                min = distance;
                minPoint = point;
            }
        }
        return minPoint;
    }
}
