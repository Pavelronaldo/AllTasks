package task7;

interface MRSW {
    void enterReader() throws InterruptedException;
    void enterWriter() throws InterruptedException;
    void exitReader();
    void exitWriter();
}
