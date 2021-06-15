package task8;

class ConTask implements Runnable{
    Buffer buffer;
    ConTask(Buffer buffer){
        this.buffer = buffer;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buffer.get();
    }
}
