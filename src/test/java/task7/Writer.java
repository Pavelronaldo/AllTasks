package task7;

import java.util.Random;

public class Writer extends Thread {
    private final ReadWriteList<Integer> sharedList;

    public Writer(ReadWriteList<Integer> sharedList) {
        this.sharedList = sharedList;
    }

    @Override
    public void run() {
        Random random = new Random();
        int number = random.nextInt(100);
        try {
            sharedList.add(number);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(getName() + " -> put: " + number);
    }
}
