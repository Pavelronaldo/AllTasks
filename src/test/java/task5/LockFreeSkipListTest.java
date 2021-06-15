package task5;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LockFreeSkipListTest {
    @Test
    public void lockFreeSkipListTest() {
        Buffer buffer = new Buffer();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.execute(new ProdTask(buffer));
        for (int i = 0; i < 999; i++) {
            executor.execute(new ConTask(buffer, new AtomicInteger(i)));
        }
        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(1, (int) buffer.size());
        Assert.assertTrue(buffer.contains(999));
    }
}