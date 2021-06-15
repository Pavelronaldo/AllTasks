package task7;

public class MRSWImpl implements MRSW {
    private int numReaders = 0;
    private int numWriters = 0;

    synchronized public void enterReader() throws InterruptedException
    {
        while (numWriters > 0) wait();
        numReaders++;
    }
    synchronized public void enterWriter() throws InterruptedException
    {
        while ((numWriters > 0) || (numReaders > 0)) wait();
        numWriters++;
    }

    synchronized public void exitReader() {
        numReaders--;
        notifyAll();
    }
    synchronized public void exitWriter() {
        numWriters--;
        notifyAll();
    }
}
