import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final Point[] copy;
    private final Point[] points;
    private final LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        validateInput(points);
        this.copy = points.clone();
        this.points = points.clone();
        this.lineSegments = mutableSegments();
    }

    public static void main(String[] args) {

        int n = StdIn.readInt();
        Point[] points = new Point[n];
        int i = 0;
        while (!StdIn.isEmpty()) points[i++] = new Point(StdIn.readInt(), StdIn.readInt());
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-1000, 32768);
        StdDraw.setYscale(-1000, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        FastCollinearPoints bcp = new FastCollinearPoints(points);

        for (LineSegment segment : bcp.segments()) {
            StdOut.println(segment);
            segment.draw();
            StdDraw.show();
        }
    }

    public int numberOfSegments() {
        return lineSegments.length;
    }

    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    private void validateInput(Point[] pp) {
        nullCheck(pp);
        duplicateCheck(pp);
    }

    private void nullCheck(Point[] pp) {
        if (pp == null) throw new IllegalArgumentException();
        for (Point p : pp) {
            if (p == null) throw new IllegalArgumentException();
        }
    }

    private void duplicateCheck(Point[] pp) {
        Point[] pointS = pp.clone();
        Arrays.sort(pointS, new Point(-1, -1).slopeOrder());
        Point cur = pointS[0];
        boolean flag = true;
        for (Point p : pointS) {
            if (flag) {
                flag = false;
                continue;
            }
            if (cur.compareTo(p) == 0) throw new IllegalArgumentException();
            cur = p;
        }
    }

    private LineSegment[] mutableSegments() {
        List<LineSegment> listOfSegments = new ArrayList<>();
        int cnter;
        for (int p = 0; p < points.length; p++) {
            Point pivot = points[p];
            Arrays.sort(copy);
            Arrays.sort(copy, pivot.slopeOrder());
            for (int q = 1; q < points.length - 1; q += cnter + 1) {
                cnter = 0;
                int right = q + 1;
                Point current = copy[q];
                while (pivot.slopeTo(current) == pivot.slopeTo(copy[right])) {
                    current = copy[right];
                    cnter++;
                    if (right + 1 == points.length) break;
                    right++;
                }

                if (cnter >= 2) {
                    boolean isRepeated = false;
                    for (int k = q; k <= q + cnter; k++) {
                        if (pivot.compareTo(copy[k]) > 0) {
                            isRepeated = true;
                            break;
                        }
                    }
                    if (!isRepeated) {
                        listOfSegments.add(new LineSegment(pivot, current));
                    }
                }
            }
        }
        return listOfSegments.toArray(new LineSegment[0]);
    }
}
