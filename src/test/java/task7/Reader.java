package task7;


import java.util.Random;

public class Reader extends Thread {
    private final ReadWriteList<Integer> sharedList;

    public Reader(ReadWriteList<Integer> sharedList) {
        this.sharedList = sharedList;
    }

    @Override
    public void run() {
        Random random = new Random();
        int index, number = 0;
        try {
            index = random.nextInt(sharedList.size());
            number = sharedList.get(index);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getName() + " -> get: " + number);

        try {
            Thread.sleep(500);
        } catch (InterruptedException ie ) { ie.printStackTrace(); }

    }
}
