package task5;

class ProdTask implements Runnable {
    Buffer buffer;
    ProdTask(Buffer buffer){
        this.buffer = buffer;
    }
    @Override
    public void run() {
        for(int i = 0; i < 1000; i++){
            buffer.put(i);
        }
    }
}
