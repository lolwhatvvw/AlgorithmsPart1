import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false; //  optimize: remove this variable, replace in code with !VERTICAL
    private Node root;
    private int size;

    public static void main(String[] args) {
        //
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p) != null;
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) return;
        draw(x.lb);
        x.p.draw();
        if (x.direction == VERTICAL) {
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.point(x.p.x(), x.p.y());
        } else {
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.point(x.p.x(), x.p.y());
        }
        draw(x.rt);
    }

    private Node get(Node x, Point2D point) {
        while (x != null) {
            if (x.p.equals(point)) return x; // place to optimize
            if (x.direction == VERTICAL) {
                int cmp = Double.compare(point.x(), x.p.x());
                if (cmp < 0) x = x.lb;
                else x = x.rt;
            } else { // if (x.direction == HORIZONTAL) {
                int cmp = Double.compare(point.y(), x.p.y());
                if (cmp < 0) x = x.lb;
                else x = x.rt;
            }
        }
        return null;
    }

    public void insert(Point2D p) {
        if (contains(p)) return;
        root = simpleInsert(root, p, VERTICAL, 0, 0, 1, 1);
    }

    private Node simpleInsert(Node x, Point2D point, boolean direction,
                              double xmin, double ymin, double xmax, double ymax) {

        if (x == null) {
            Node node = new Node(point, direction, new RectHV(xmin, ymin, xmax, ymax));
            size++;
            return node;
        }
        if (x.direction == VERTICAL) {
            int cmp = Double.compare(point.x(), x.p.x());
            if (cmp < 0)
                x.lb = simpleInsert(x.lb, point, HORIZONTAL, xmin, ymin, x.p.x(), ymax); //  taking left part of the rectangle so limiting xmax
            else
                x.rt = simpleInsert(x.rt, point, HORIZONTAL, x.p.x(), ymin, xmax, ymax); //  taking right part of the rectangle so limiting xmin
        } else { // if (x.direction == HORIZONTAL) {
            int cmp = Double.compare(point.y(), x.p.y());
            if (cmp < 0)
                x.lb = simpleInsert(x.lb, point, VERTICAL, xmin, ymin, xmax, x.p.y()); // taking bottom part of the rectangle so limiting ymax
            else
                x.rt = simpleInsert(x.rt, point, VERTICAL, xmin, x.p.y(), xmax, ymax);  // taking top part of the rectangle so limiting ymin
        }

        return x;
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> stack = new Stack<>();
        range(root, rect, stack);
        return stack;
    }

    private void range(Node x, RectHV rect, Stack<Point2D> stack) {
        if (x == null) return;
        if (rect.intersects(x.rect)) { // root always intersects cause whole plane in rectangle
            if (rect.contains(x.p)) stack.push(x.p);
        } else return;
        range(x.lb, rect, stack);
        range(x.rt, rect, stack);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Node best = new Node(root.p, false, null);
        nearest(root, p, best);
        return best.p;
    }

    private void nearest(Node x, Point2D query, Node nearest) {
        if (x == null) return;

        if (query.distanceSquaredTo(x.p) < nearest.p.distanceSquaredTo(query)) {
            nearest.p = x.p;
        }

        if (nearest.p.distanceSquaredTo(query) < x.rect.distanceSquaredTo(query)) return;

        double queryX = query.x();
        double queryY = query.y();

        double currentPointX = x.p.x();
        double currentPointY = x.p.y();

        RectHV leftOrBottomSubtree = null;
        RectHV rightOrTopSubtree = null;
        if (x.direction == VERTICAL) {
            if (queryX >= currentPointX) { //  check the assumption that we need to search right subtree if query located on the right side
                if (x.lb != null) leftOrBottomSubtree = x.lb.rect; //  remember alternative if we have it
                nearest(x.rt, query, nearest);
                if (leftOrBottomSubtree != null && leftOrBottomSubtree.distanceSquaredTo(query) < query.distanceSquaredTo(nearest.p)) {
                    nearest(x.lb, query, nearest); // if the assumption was wrong
                }
            } else { // the same but for the alternative - check first the left subtree
                if (x.rt != null) rightOrTopSubtree = x.rt.rect;
                nearest(x.lb, query, nearest);
                if (rightOrTopSubtree != null && rightOrTopSubtree.distanceSquaredTo(query) < query.distanceSquaredTo(nearest.p)) {
                    nearest(x.rt, query, nearest);
                }
            }
            // the same but depending on another direction
        } else { //  if (x.direction == HORIZONTAL) {
            if (queryY >= currentPointY) {
                if (x.lb != null) leftOrBottomSubtree = x.lb.rect;
                nearest(x.rt, query, nearest);
                if (leftOrBottomSubtree != null && leftOrBottomSubtree.distanceSquaredTo(query) < query.distanceSquaredTo(nearest.p)) {
                    nearest(x.lb, query, nearest);
                }
            } else {
                if (x.rt != null) rightOrTopSubtree = x.rt.rect;
                nearest(x.lb, query, nearest);
                if (rightOrTopSubtree != null && rightOrTopSubtree.distanceSquaredTo(query) < query.distanceSquaredTo(nearest.p)) {
                    nearest(x.rt, query, nearest);
                }
            }
        }
    }

    private static class Node {
        private final boolean direction; // the direction of the axis
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Point2D p;      // the point
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        Node(Point2D p, boolean direction, RectHV rect) {
            this.p = p;
            this.rect = rect;
            this.direction = direction;
        }
    }
}