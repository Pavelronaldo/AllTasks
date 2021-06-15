package task8;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MichaelScottQueue<E> {

    private static class Node<E> {
        final E item;
        final AtomicReference<Node<E>> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = new AtomicReference<>(next);
        }
    }

    private final Node<E> dummy = new Node<>(null, null);

    private final AtomicReference<Node<E>> head  = new AtomicReference<>(dummy);
    private final AtomicReference<Node<E>> tail  = new AtomicReference<>(dummy);

    private final AtomicInteger size;

    public MichaelScottQueue() {
        size = new AtomicInteger(0);
    }

    public void add(E item) {
        Node<E> toPut = new Node<>(item, null);
        while (true) {
            Node<E> currTail = tail.get();
            Node<E> tailNext = currTail.next.get();
            if (currTail == tail.get()) {
                if (tailNext == null) {
                    if (currTail.next.compareAndSet(null, toPut)) {
                        tail.compareAndSet(currTail, toPut);
                        size.getAndIncrement();
                        return;
                    }
                } else {
                    tail.compareAndSet(currTail, tailNext);
                }
            }
        }
    }

    public E poll() {
        while(true) {
            Node<E> tempHead = head.get();
            Node<E> tempTail = tail.get();
            Node<E> headNext = tempHead.next.get();
            if(tempHead == head.get()) {
                if(tempHead == tempTail) {
                    if(headNext == null) {
                        return null;
                    }
                    tail.compareAndSet(tempTail, headNext);
                } else {
                    E retNode = headNext.item;
                    if (head.compareAndSet(tempHead, headNext)) {
                        size.getAndDecrement();
                        return retNode;
                    }
                }
            }
        }
    }

    public int size() {
        return size.get();
    }
}
