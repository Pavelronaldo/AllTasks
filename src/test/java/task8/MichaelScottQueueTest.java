package task8;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MichaelScottQueueTest {
    @Test
    public void test() {
        Buffer buffer = new Buffer();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.execute(new ProdTask(buffer));
        for (int i = 0; i < 1000; i++) {
            executor.execute(new ConTask(buffer));
        }
        executor.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(0, (int) buffer.size());
    }
}