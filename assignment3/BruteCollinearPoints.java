/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description: Very bad code
 **************************************************************************** */

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;
    private final Point[] points;
    private int size;

    public BruteCollinearPoints(Point[] pp) {
        validateInput(pp);
        this.points = copy(pp);
        Arrays.sort(points, new Point(-1, -1).slopeOrder());
        size = 0;
        lineSegments = mutableSegments();
    }

    public static void main(String[] args) {
        //
    }

    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    public int numberOfSegments() {
        return size;
    }

    private Point[] copy(Point[] pp) {
        return pp.clone();
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
        Point[] pointS = copy(pp);
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
        List<LineSegment> aList = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int z = k + 1; z < points.length; z++) {
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])
                                && points[i].slopeTo(points[k]) == points[i].slopeTo(points[z])
                        ) {
                            aList.add(new LineSegment(points[z], points[i]));
                            size++;
                        }
                    }
                }
            }
        }
        LineSegment[] nLs = new LineSegment[size];
        return aList.toArray(nLs);
    }
}
