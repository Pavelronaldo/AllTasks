package task7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadWriteList<E> {
    private final List<E> list = new ArrayList<>();
    private final MRSWImpl rwLock = new MRSWImpl();

    @SafeVarargs
    public ReadWriteList(E... initialElements) {
        list.addAll(Arrays.asList(initialElements));
    }

    public void add(E element) throws InterruptedException {
        rwLock.enterWriter();

        try {
            list.add(element);
        } finally {
            rwLock.exitWriter();
        }
    }

    public E get(int index) throws InterruptedException {
        rwLock.enterReader();

        try {
            return list.get(index);
        } finally {
            rwLock.exitReader();
        }
    }

    public int size() throws InterruptedException {
        rwLock.enterReader();

        try {
            return list.size();
        } finally {
            rwLock.exitReader();
        }
    }

}
