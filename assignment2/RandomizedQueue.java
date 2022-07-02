/* *****************************************************************************
 *  Name:       Vladimir Vovk
 *  Date:        6/8/2022
 *  Description:  Implementing Deque using doubly-linked list
                    and randomized queue using array
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INITIAL_CAPACITY = 2;
    private Item[] q;
    private int head;
    private int tail;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        q = (Item[]) new Object[INITIAL_CAPACITY];
        head = 0;
        tail = 0;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("HQEIOTAHBJ");
        rq.enqueue("NTNBIXNWEH");
        rq.enqueue("DSQGPPMWHU");
        rq.enqueue("QQTDERFMAR");
        rq.enqueue("IVXKLINGCH");
        rq.enqueue("YBQHHQMVQQ");
        rq.enqueue("IDIQJPQKLN");
        rq.enqueue("BPCNVZJVAK");
        rq.enqueue("RWFEBRRJRX");
        System.out.println(rq.remove(rq.tail - 1));
        System.out.println(rq.getIndex(rq.tail));

        for (Iterator<String> it = rq.iterator(); it.hasNext(); ) {
            System.out.println(it.next());

        }
    }

    private void resize(int capacity) {
        Item[] newq = (Item[]) new Object[capacity];
        for (int i = head, j = 0; i < tail; i++, j++) {
            newq[j] = q[i];
        }
        q = newq;
        head = 0;
        tail = size; // most certainly size and tail always the same xD, separating them for confidence
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        // probably not right, invariant: tail always points on null, tail-1 is not null until size == 1
        // after enqueue with precondition tail == len(q), outOfBounds thrown
        if (tail + 1 == q.length) {
            resize(2 * q.length);
        }
        q[tail++] = item;
        size++;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int random = StdRandom.uniform(head, tail);
        Item item = q[random];
        if (random == head) {
            q[head++] = null;
        } else if (random == tail - 1) {
            q[--tail] = null;
        } else {
            Item tmp = q[tail - 1];
            q[random] = tmp;
            q[--tail] = null;
        }
        size--;
        if (size > 0 && size == q.length / 4) resize(q.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return q[StdRandom.uniform(head, tail)];
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private int start;
            private int end;
            private Item[] randomQ = shuffledCopy();

            private Item[] shuffledCopy() {
                Item[] cop = (Item[]) new Object[size];
                int j = 0;
                for (int i = head; i < tail; i++) {
                    cop[j++] = q[i];
                }
                StdRandom.shuffle(cop);
                start = 0;
                end = j;
                return cop;
            }

            public boolean hasNext() {
                return start != end;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                return randomQ[start++];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

}