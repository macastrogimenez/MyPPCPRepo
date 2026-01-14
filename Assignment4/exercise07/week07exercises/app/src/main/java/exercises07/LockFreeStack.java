// For week 7
// raup@itu.dk * 2023-10-20
package exercises07;

import java.util.concurrent.atomic.AtomicReference;

class LockFreeStack<T> {
    AtomicReference<Node<T>> top = new AtomicReference<Node<T>>(); 

    public void push(T value) {
        Node<T> newHead = new Node<T>(value);
        Node<T> oldHead;
        do {
            oldHead      = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead,newHead));

    }

    public T pop() {
        Node<T> newHead;
        Node<T> oldHead;
        do {
            oldHead = top.get();
            if(oldHead == null) { return null; }
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead,newHead));

        return oldHead.value;
    }

    private static class Node<T> {
        public final T value;
        public Node<T> next;

        public Node(T value) {
            this.value = value;
            this.next  = null;
        }
    }
}
