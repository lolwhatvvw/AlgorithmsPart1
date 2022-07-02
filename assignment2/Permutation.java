import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

/* *****************************************************************************
 *  Name:       Vladimir Vovk
 *  Date:        6/8/2022
 *  Description:  Implementing Deque using doubly-linked list
                    and randomized-queue using array
                    Permutation.java pass bonus memory test using reservoir sampling
 **************************************************************************** */

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> r = new RandomizedQueue<>();
        int cnter = 0;
        while (cnter < k) {
            r.enqueue(StdIn.readString());
            cnter++;
        }
        int i = 0;
        while (!StdIn.isEmpty()) {
            i++;
            if (StdRandom.bernoulli((double) k / (k + i))) {
                r.dequeue();
                r.enqueue(StdIn.readString());
            } else {
                StdIn.readString();
            }
        }
        int j = 0;
        for (Iterator<String> it = r.iterator(); it.hasNext() && j < k; j++) {
            StdOut.println(it.next());
        }
    }
}
