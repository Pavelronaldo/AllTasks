package task8;

import java.util.PriorityQueue;
import java.util.Queue;

class Buffer {
    Integer i;
    MichaelScottQueue<Integer> queue = new MichaelScottQueue<>();
    //Queue<Integer> queue = new PriorityQueue<>();

    public void get(){
        System.out.println("Consumer recd - " + queue.poll());
    }
    public void put(Integer i){
        this.i = i;
        queue.add(i);
        System.out.println("Putting - " + i);
    }

    public Integer size() {
        return queue.size();
    }
}
