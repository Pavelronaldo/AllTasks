package task6;

import task6.util.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Disruptor<T> {
    private final ObjectWrapper<T>[] ringBuffer;
    private final int BUFFER_SIZE;
    private final AtomicInteger numReaders = new AtomicInteger(0);
    private volatile long writerIndex = 1;
    private volatile long lastReader = 1;
    private final AtomicBoolean writerAssigned = new AtomicBoolean(false);
    private final AtomicBoolean alreadyStarted = new AtomicBoolean(false);


    public Disruptor() {
        BUFFER_SIZE = 1024;
        ringBuffer = new ObjectWrapper[BUFFER_SIZE];
    }

    public Disruptor(int bufferSize) {
        BUFFER_SIZE = getPowerOfTwo(bufferSize);
        ringBuffer = new ObjectWrapper[BUFFER_SIZE];
    }

    public DisruptorReader getReader() throws DisruptorAlreadyStartedException {
        if (alreadyStarted.get()) {
            throw new DisruptorAlreadyStartedException("Cannot get a reader after the disruptor has already started.");
        }
        numReaders.incrementAndGet();
        return new DisruptorReader();
    }

    public DisruptorWriter getWriter() throws DisruptorAlreadyStartedException, WriterAlreadyAssignedException {
        if (alreadyStarted.get()) {
            throw new DisruptorAlreadyStartedException("Cannot get a writer after the disruptor has already started.");
        }
        if (writerAssigned.getAndSet(true)) {
            throw new WriterAlreadyAssignedException("A writer has already been assigned for this disruptor.");
        }

        return new DisruptorWriter();
    }

    public void initialise() {
        alreadyStarted.set(true);
        for (int i = 0; i < BUFFER_SIZE; i++) {
            ringBuffer[i] = new ObjectWrapper<>(0);
        }
    }

    protected int getPowerOfTwo(int bufferSize) {
        return Integer.highestOneBit(bufferSize);
    }

    private void writeToBuffer(T t) {
        while (writerIndex - lastReader >= BUFFER_SIZE) {}
        ringBuffer[(int) writerIndex % BUFFER_SIZE].setContent(t, numReaders.get());
        writerIndex++;
    }

    private T readValue(long idx) {
        incrementReaderCount(idx - 1);
        while (idx >= writerIndex) {}
        return ringBuffer[(int) idx % BUFFER_SIZE].getContent();
    }

    private void incrementReaderCount(long idx) {
        if (ringBuffer[(int) idx % BUFFER_SIZE].beenRead()) {
            lastReader = idx + 1;
        }
    }

    private class DisruptorReader implements Reader<T> {
        private volatile long currentCount = 1;

        @Override
        public T readNext() {
            return readValue(currentCount++);
        }
    }

    private class DisruptorWriter implements Writer<T> {
        @Override
        public void writeNext(T t) {
            writeToBuffer(t);
        }
    }

}
