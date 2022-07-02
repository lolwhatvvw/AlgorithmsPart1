import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Using two WQU to solve back wash problem
 * 100 score but doesn't pass bonus memory test
 */
public class PercolationV2 {
    private final boolean[] isOpen;
    private final int size;
    private final int top;
    private final int bottom;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf1;
    private int cnt = 0;

    // creates n-by-n grid, with all sites initially blocked
    public PercolationV2(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        size = n;
        top = n * n;
        bottom = n * n + 1;
        isOpen = new boolean[n * n];
        uf = new WeightedQuickUnionUF(n * n + 2); // uf using two virtual nodes: top and bottom
        uf1 = new WeightedQuickUnionUF(n * n + 1); // uf1 using one virtual node - top
        virtualNodesInit();
    }


    /**
     * flattening 2d array
     * invariant:
     *
     * @param row ∈ [1, n]
     * @param col ∈ [1, n]
     * @return position from range [0, n*n-1]
     * ex: getIndex(1, 1) should be (0)
     * ex: getIndex(n, n) should be (n*n -1)
     */
    private int getIndex(int row, int col) {
        return (row - 1) * size + col - 1;
    }

    /**
     * connecting first row to the top for uf and uf1
     * connecting last row to the bottom for uf
     */
    private void virtualNodesInit() {
        for (int i = 0; i < size; i++) {
            uf.union(top, i);
            uf1.union(top, i);
            uf.union(bottom, getIndex(size, i + 1));
        }
    }

    /**
     * checking input from client
     * invariant:
     *
     * @param row ∈ [1, n]
     * @param col ∈ [1, n]
     *            throws exception, but it is not allowed for the assignment
     */
    private void validate(int row, int col) {
        if (
                row > size || row < 1
                        || col > size || col < 1
        ) throw new IllegalArgumentException();
    }

    /**
     * checking access to array with row +- 1 and col +-1
     * invariant:
     *
     * @param row +- 1 ∈ [0, n-1]
     * @param col +- 1 ∈ [0, n-1]
     * @return false if violating
     */
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
        int cur = getIndex(row, col);
        if (isValid(row, col - 1) && isOpen(row, col - 1)) {
            uf.union(cur, getIndex(row, col - 1));
            uf1.union(cur, getIndex(row, col - 1));
        }
        if (isValid(row, col + 1) && isOpen(row, col + 1)) {
            uf.union(cur, getIndex(row, col + 1));
            uf1.union(cur, getIndex(row, col + 1));
        }
        if (isValid(row - 1, col) && isOpen(row - 1, col)) {
            uf.union(cur, getIndex(row - 1, col));
            uf1.union(cur, getIndex(row - 1, col));
        }
        if (isValid(row + 1, col) && isOpen(row + 1, col)) {
            uf.union(cur, getIndex(row + 1, col));
            uf1.union(cur, getIndex(row + 1, col));
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return isOpen[getIndex(row, col)] && (uf1.find(getIndex(row, col)) == uf1.find(top));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return cnt;
    }

    // does the system percolate?
    public boolean percolates() {
        return size == 1 ? isOpen(1, 1) && uf.find(top) == uf.find(bottom)
                : uf.find(top) == uf.find(bottom);
    }

    public static void main(String[] args) {
        //
    }
}