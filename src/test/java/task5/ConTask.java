package task5;

import java.util.concurrent.atomic.AtomicInteger;

class ConTask implements Runnable {
    AtomicInteger i;
    Buffer buffer;
    ConTask(Buffer buffer, AtomicInteger i){
        this.buffer = buffer;
        this.i = i;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buffer.get(i.get());
    }
}
