import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String str = "";
        int i = 0;
        while (!StdIn.isEmpty()) {
            String currString = StdIn.readString();
            str = StdRandom.bernoulli((double) 1 / ++i) ? currString : str;
        }
        StdOut.println(str);
    }
}
