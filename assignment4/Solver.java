/* *****************************************************************************
 *  Name:
 *  Date: 6/23/2022
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// todo use one pq

public class Solver {
    private final Stack<Board> solution;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        solution = new Stack<>();
        Board twin = initial.twin();

        SearchNode currentTwin = new SearchNode(twin, 0);
        currentTwin.prev = null;

        SearchNode current = new SearchNode(initial, 0);
        current.prev = null;

        MinPQ<SearchNode> pqInit = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();

        pqInit.insert(current);
        pqTwin.insert(currentTwin);

        while (true) {
            if (!(current = pqInit.delMin()).board.isGoal()) {
                for (Board b : current.board.neighbors()) {
                    if (current.prev == null || !current.prev.board.equals(b)) {
                        SearchNode neighbour = new SearchNode(b, current.moves + 1);
                        neighbour.prev = current;
                        pqInit.insert(neighbour);
                    }
                }
            } else {
                solvable = true;
                break;
            }
            if (!(currentTwin = pqTwin.delMin()).board.isGoal()) {
                for (Board b : currentTwin.board.neighbors()) {
                    if (currentTwin.prev == null || !currentTwin.prev.board.equals(b)) {
                        SearchNode neighbour = new SearchNode(b, currentTwin.moves + 1);
                        neighbour.prev = currentTwin;
                        pqTwin.insert(neighbour);
                    }
                }
            } else {
                solvable = false;
                break;
            }
        }
        current = solvable ? current : currentTwin;
        for (; current != null; current = current.prev)
            solution.push(current.board);
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        int n = StdIn.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = StdIn.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        // use search twice on twin and init and break flag YE null -1 for solution
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solvable ? solution.size() - 1 : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solvable ? solution : null;
    }

    private static class SearchNode implements Comparable<SearchNode> {
        Board board;
        SearchNode prev;

        int moves;
        int priority;
        int manhattan;

        SearchNode(Board b, int moves) {
            this.board = b;
            this.moves = moves;
            this.manhattan = board.manhattan();
            this.priority = manhattan + this.moves;
        }

        public int compareTo(SearchNode ob) {
            int cmp = this.priority - ob.priority;
            if (cmp == 0) return this.manhattan - ob.manhattan;
            return cmp;
        }
    }
}

