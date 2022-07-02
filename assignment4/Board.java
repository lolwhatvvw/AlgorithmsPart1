/* *****************************************************************************
 *  Name:
 *  Date: 6/22/2022
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int n;
    private final short[] titles;
    private int indexOfBlock;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;
        this.titles = new short[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.titles[i * n + j] = (short) tiles[i][j];
                if (tiles[i][j] == 0) indexOfBlock = getIndex(i, j); // could use flag for optimization
            }
        }
    }

    private Board(short[] titles, int dim) {
        this.titles = titles.clone();
        this.n = dim;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        //
    }

    private int getIndex(int i, int j) {
        return i * n + j;
    }

    private int calculateCol(int index) {
        return index % n;
    }

    private int calculateRow(int index) {
        return (index - calculateCol(index)) / n;
    }

    private int calculateManhattan(int i1, int i2) {
        return Math.abs(calculateRow(i1) - calculateRow(i2)) + Math.abs(calculateCol(i1) - calculateCol(i2));
    }

    private void exchange(int i, int j) {
        short tmp = titles[i];
        titles[i] = titles[j];
        titles[j] = tmp;
    }

    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder(String.valueOf(n));
        str.append("\n");
        for (int i = 0; i < n * n; i++) {
            str.append(titles[i]).append(" ");
            if ((i + 1) % n == 0) str.append("\n");
        }
        return str.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int cnter = 0;
        for (int i = 0; i < n * n; i++) {
            if (titles[i] == 0) continue;
            if (titles[i] != i + 1) {
                cnter++;
            }
        }
        return cnter;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < n * n; i++) {
            if (titles[i] == 0) continue;
            sum += calculateManhattan(titles[i] - 1, i);
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        boolean flag = true;
        for (int i = 0; i < n * n - 1; i++) {
            if (titles[i] != i + 1) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board other = (Board) y;
        return this.dimension() == other.dimension() && Arrays.equals(this.titles, other.titles);
    }

    private boolean isValid(int row, int col) {
        if (row >= n || row < 0 || col >= n || col < 0) {
            return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int row = calculateRow(indexOfBlock);
        int col = calculateCol(indexOfBlock);
        List<Board> ls = new ArrayList<>();
        if (isValid(row, col + 1)) {
            Board right = new Board(this.titles, n);
            right.exchange(indexOfBlock, getIndex(row, col + 1));
            right.indexOfBlock = getIndex(row, col + 1);
            ls.add(right);
        }
        if (isValid(row, col - 1)) {
            Board left = new Board(this.titles, n);
            left.exchange(indexOfBlock, getIndex(row, col - 1));
            left.indexOfBlock = getIndex(row, col - 1);
            ls.add(left);
        }
        if (isValid(row + 1, col)) {
            Board up = new Board(this.titles, n);
            up.exchange(indexOfBlock, getIndex(row + 1, col));
            up.indexOfBlock = getIndex(row + 1, col);
            ls.add(up);
        }
        if (isValid(row - 1, col)) {
            Board down = new Board(this.titles, n);
            down.exchange(indexOfBlock, getIndex(row - 1, col));
            down.indexOfBlock = getIndex(row - 1, col);
            ls.add(down);
        }
        return ls;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(this.titles, n);
        twin.indexOfBlock = this.indexOfBlock;
        twin.exchange(indexOfBlock, 0);
        twin.exchange(1, 2);
        twin.exchange(indexOfBlock, 0);
        return twin;
    }

}