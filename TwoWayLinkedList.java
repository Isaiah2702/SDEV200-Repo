import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class TwoWayLinkedList<E> implements MyList<E> {

    private Node<E> head, tail;
    private int size = 0;

    // Doubly-linked node (as required)
    public static class Node<E> {
        E element;
        Node<E> next;
        Node<E> previous;

        public Node(E e) {
            element = e;
        }
    }

    // ---------- Required MyList methods ----------
    @Override
    public void add(E e) {
        addLast(e);
    }

    public void addFirst(E e) {
        Node<E> n = new Node<>(e);
        n.next = head;
        if (head != null)
            head.previous = n;
        head = n;
        if (tail == null)
            tail = head;
        size++;
    }

    public void addLast(E e) {
        Node<E> n = new Node<>(e);
        n.previous = tail;
        if (tail != null)
            tail.next = n;
        tail = n;
        if (head == null)
            head = tail;
        size++;
    }

    @Override
    public void add(int index, E e) {
        checkPosition(index);
        if (index == 0) {
            addFirst(e);
            return;
        }
        if (index == size) {
            addLast(e);
            return;
        }

        Node<E> cur = nodeAt(index);
        Node<E> prev = cur.previous;

        Node<E> n = new Node<>(e);
        n.next = cur;
        n.previous = prev;
        prev.next = n;
        cur.previous = n;

        size++;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean contains(E e) {
        return indexOf(e) != -1;
    }

    @Override
    public E get(int index) {
        checkElement(index);
        return nodeAt(index).element;
    }

    @Override
    public int indexOf(E e) {
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next, i++) {
            if (eq(cur.element, e))
                return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(E e) {
        int i = size - 1;
        for (Node<E> cur = tail; cur != null; cur = cur.previous, i--) {
            if (eq(cur.element, e))
                return i;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean remove(E e) {
        for (Node<E> cur = head; cur != null; cur = cur.next) {
            if (eq(cur.element, e)) {
                unlink(cur);
                return true;
            }
        }
        return false;
    }

    @Override
    public E remove(int index) {
        checkElement(index);
        Node<E> cur = nodeAt(index);
        E old = cur.element;
        unlink(cur);
        return old;
    }

    @Override
    public Object set(int index, E e) {
        checkElement(index);
        Node<E> cur = nodeAt(index);
        E old = cur.element;
        cur.element = e;
        return old;
    }

    @Override
    public int size() {
        return size;
    }

    // ---------- Iterators ----------
    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    public ListIterator<E> listIterator() {
        return new DLLIterator(0);
    }

    public ListIterator<E> listIterator(int index) {
        checkPosition(index);
        return new DLLIterator(index);
    }

    private class DLLIterator implements ListIterator<E> {
        private Node<E> nextNode; // what next() will return
        private Node<E> lastReturned; // last returned by next/previous
        private int nextIndex; // index of nextNode

        DLLIterator(int index) {
            nextIndex = index;
            nextNode = (index == size) ? null : nodeAt(index);
            lastReturned = null;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();
            lastReturned = nextNode;
            nextNode = nextNode.next;
            nextIndex++;
            return lastReturned.element;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();

            // if cursor is at end (nextNode == null), previous is tail
            nextNode = (nextNode == null) ? tail : nextNode.previous;

            lastReturned = nextNode;
            nextIndex--;
            return lastReturned.element;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();

            // If lastReturned came from next(), cursor is after it -> shift back
            if (lastReturned != nextNode)
                nextIndex--;
            // If lastReturned came from previous(), nextNode currently points to it -> move
            // nextNode forward
            else
                nextNode = nextNode.next;

            unlink(lastReturned);
            lastReturned = null;
        }

        @Override
        public void set(E e) {
            if (lastReturned == null)
                throw new IllegalStateException();
            lastReturned.element = e;
        }

        @Override
        public void add(E e) {
            // Insert at cursor (before nextNode)
            TwoWayLinkedList.this.add(nextIndex, e);
            nextIndex++;
            lastReturned = null;
        }
    }

    // ---------- Small helpers ----------
    private Node<E> nodeAt(int index) {
        Node<E> cur = head;
        for (int i = 0; i < index; i++)
            cur = cur.next;
        return cur;
    }

    private void unlink(Node<E> cur) {
        Node<E> p = cur.previous;
        Node<E> n = cur.next;

        if (p == null)
            head = n;
        else
            p.next = n;
        if (n == null)
            tail = p;
        else
            n.previous = p;

        size--;
    }

    private void checkElement(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void checkPosition(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private boolean eq(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}
