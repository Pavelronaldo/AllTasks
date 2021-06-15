package task6;

import org.junit.Test;
import task6.util.DisruptorAlreadyStartedException;
import task6.util.Reader;
import task6.util.Writer;
import task6.util.WriterAlreadyAssignedException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DisruptorTest {
    @Test
    public void testAddToAndRetrieveFromDisruptor() throws DisruptorAlreadyStartedException, WriterAlreadyAssignedException {
        final Disruptor<String> disruptor = new Disruptor<>(1024);

        final Reader<String> fxReader = disruptor.getReader();
        final Writer<String> fxWriter = disruptor.getWriter();

        disruptor.initialise();

        new Thread(() -> {
            for (int i = 0; i < 1024; i++) {
                fxWriter.writeNext("Example " + i);
            }
        }).start();

        for (int i = 0; i < 1024; i++) {
            assertEquals("Example " + i, fxReader.readNext());
        }
    }


    @Test
    public void testDoesntOverwriteUnreadData() throws InterruptedException, DisruptorAlreadyStartedException, WriterAlreadyAssignedException {
        final Disruptor<String> disruptor = new Disruptor<>(256);
        final Reader<String> fxReader = disruptor.getReader();
        final Writer<String> fxWriter = disruptor.getWriter();
        final CountDownLatch testFinished = new CountDownLatch(2);
        final int TEST_SIZE = 1000000; //Purposefully large in an attempt to flag any overtaking issues.

        disruptor.initialise();

        new Thread(() -> {
            for (int i = 0; i < TEST_SIZE; i++) {
                fxWriter.writeNext("Example " + i);
            }

            testFinished.countDown();
        }).start();

        for (int i = 0; i < TEST_SIZE; i++) {
            assertEquals("Example " + i, fxReader.readNext());
        }

        testFinished.countDown();
        testFinished.await();
    }

    @Test
    public void testMultipleReaders() throws DisruptorAlreadyStartedException, InterruptedException, WriterAlreadyAssignedException {
        Disruptor<String> disruptor = new Disruptor<>(1024);
        final Reader<String> firstReader = disruptor.getReader();
        final Reader<String> secondReader = disruptor.getReader();
        final Writer<String> writer = disruptor.getWriter();
        final AtomicBoolean firstPassed = new AtomicBoolean(true);
        final AtomicBoolean secondPassed = new AtomicBoolean(true);
        final int TEST_SIZE = 2048;

        final CountDownLatch testFinished = new CountDownLatch(3);

        disruptor.initialise();

        new Thread(() -> {
            for (int i = 0; i < TEST_SIZE; i++) {
                String val = firstReader.readNext();
                if (!val.equals("TEST " + i)) {
                    firstPassed.set(false);
                }
            }
            testFinished.countDown();
        }).start();

        new Thread(() -> {
            for (int i = 0; i < TEST_SIZE; i++) {
                String val = secondReader.readNext();
                if (!val.equals("TEST " + i)) {
                    System.out.println("Test 2 failed on :" + i);
                    secondPassed.set(false);
                }
            }
            testFinished.countDown();
        }).start();

        new Thread(() -> {
            for (int i = 0; i < TEST_SIZE; i++) {
                writer.writeNext("TEST " + i);
            }
            testFinished.countDown();
        }).start();

        testFinished.await();

        assertTrue(firstPassed.get());
        assertTrue(secondPassed.get());
    }

    @Test
    public void testPowerOfTwo() {
        Disruptor<String> disruptor = new Disruptor<>();

        assertEquals(1024, disruptor.getPowerOfTwo(1533));
        assertEquals(0, disruptor.getPowerOfTwo(0));
        assertEquals(2, disruptor.getPowerOfTwo(3));
        assertEquals(2048, disruptor.getPowerOfTwo(2048));
    }
}
