package task7;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ReadWriteLockTest {
    static final int READER_SIZE = 10;
    static final int WRITER_SIZE = 2;
    @Test
    public void rwTest() throws InterruptedException {

        ReadWriteList<Integer> sharedList = new ReadWriteList<>(33, 28, 86, 99);
        List<Writer> writers = new ArrayList<>();
        List<Reader> readers = new ArrayList<>();

        for (int i = 0; i < WRITER_SIZE; i++) {
            writers.add(new Writer(sharedList));
        }
        for (int i = 0; i < READER_SIZE; i++) {
            readers.add(new Reader(sharedList));
        }

        writers.forEach(Thread::start);
        readers.forEach(Thread::start);

        for (Writer writer : writers) writer.join();
        for (Reader reader : readers) reader.join();
    }
}
