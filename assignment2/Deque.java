import java.util.Iterator;
import java.util.NoSuchElementException;

/* *****************************************************************************
 *  Name:       Vladimir Vovk
 *  Date:        6/8/2022
 *  Description:  Implementing Deque using doubly-linked list
                    and randomized-queue using array
 **************************************************************************** */
public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // unit testing (required)
    public static void main(String[] args) {
        //
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst;
        if (first == null) {
            first = new Node();
            first.item = item;
            if (isEmpty()) {
                last = first;
            }
        } else {
            oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            oldFirst.prev = first;
            if (isEmpty()) {
                last = first;
            }
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = null;
        if (last != null) {
            oldLast = last;
        }
        last = new Node();
        last.item = item;
        if (isEmpty()) first = last;
        else {
            if (oldLast != null) {
                oldLast.next = last;
                last.prev = oldLast;
            }
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.item;
        size--;
        if (isEmpty()) {
            first = null;
            last = null;
        } else {
            Node second = first.next;
            second.prev = null;
            first = second;
        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = last.item;
        size--;
        last = last.prev;
        if (isEmpty()) first = null;
        else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node cur = first;

            public boolean hasNext() {
                return cur != null;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                Item item = cur.item;
                cur = cur.next;
                return item;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private class Node {
        Item item;
        Node next;
        Node prev;
    }
}
