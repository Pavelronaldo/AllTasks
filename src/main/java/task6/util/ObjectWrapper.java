package task6.util;

import java.util.concurrent.atomic.AtomicInteger;

public class ObjectWrapper<T> {
    private T content;
    private final AtomicInteger readBy = new AtomicInteger();

    public ObjectWrapper(int readBy) {
        this.readBy.set(readBy);
    }

    public void setContent(T t, int numReaders) {
        content = t;
        readBy.set(numReaders);
    }

    public T getContent() {
        readBy.decrementAndGet();
        return content;
    }

    public boolean beenRead() {
        return readBy.intValue() < 1;
    }
}