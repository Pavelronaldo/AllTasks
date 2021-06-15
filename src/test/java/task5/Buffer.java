package task5;

import java.util.PriorityQueue;
import java.util.Queue;

class Buffer {
    Integer i;
    //LockFreeSkipList<Integer> list = new LockFreeSkipList<>();
    Queue<Integer> list = new PriorityQueue<>();

    public void get(Integer i){
        System.out.println("Consumer recd - " + list.remove(i));
    }

    public void put(Integer i){
        this.i = i;
        list.add(i);
        System.out.println("Putting - " + i);
    }

    public Integer size() {
        return list.size();
    }

    public boolean contains(Integer i) {
        return list.contains(i);
    }
}
