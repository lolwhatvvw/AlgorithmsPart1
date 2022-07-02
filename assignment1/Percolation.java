import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * passes through bonus memory test but fail on number of calls open() uf.find() calls, only 96 score
 */
public class Percolation {
    private static final int TOP = 0;
    private final int size;
    private final boolean[] isOpen;
    private final boolean[] bottomOpened;
    private final WeightedQuickUnionUF uf;
    private boolean percolates;
    private int cnt;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        percolates = false;
        bottomOpened = new boolean[n];
        isOpen = new boolean[n * n];
        size = n;
        uf = new WeightedQuickUnionUF(n * n + 1);
        cnt = 0;
        virtual();
    }

    public static void main(String[] args) {
        //
    }

    private int getIndex(int row, int col) {
        return (row - 1) * size + col - 1;
    }

    private void virtual() {
        for (int i = 0; i < size; i++) {
            uf.union(TOP, i);
        }
    }

    private void validate(int row, int col) {
        if (
                row > size || row < 1
                        || col > size || col < 1
        ) throw new IllegalArgumentException();
    }

    private boolean isValid(int row, int col) {
        if (row - 1 >= size || row - 1 < 0 || col - 1 >= size || col - 1 < 0) {
            return false;
        }
        return true;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return isOpen[getIndex(row, col)];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) return;
        cnt++;
        isOpen[getIndex(row, col)] = true;
        if ((row == size) && !percolates) {
            bottomOpened[col - 1] = true;
        }
        int cur = getIndex(row, col);
        if (isValid(row, col - 1) && isOpen(row, col - 1)) {
            uf.union(cur, getIndex(row, col - 1));
        }
        if (isValid(row, col + 1) && isOpen(row, col + 1)) {
            uf.union(cur, getIndex(row, col + 1));
        }
        if (isValid(row - 1, col) && isOpen(row - 1, col)) {
            uf.union(cur, getIndex(row - 1, col));
        }
        if (isValid(row + 1, col) && isOpen(row + 1, col)) {
            uf.union(cur, getIndex(row + 1, col));
        }
        if (!percolates && (uf.find(getIndex(row, col)) == uf.find(TOP))) {
            for (int i = 1; (i <= size); i++) {
                if (bottomOpened[i - 1] && (uf.find(getIndex(row, col)) == uf.find(getIndex(size, i))))
                    percolates = true;
            }
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return isOpen[getIndex(row, col)] && (uf.find(getIndex(row, col)) == uf.find(TOP));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return cnt;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolates;
        /* size == 1 ? isOpen(1, 1) && uf.find(TOP) == uf.find(bottom)
                            : uf.find(TOP) == uf.find(bottom);*/
    }
}