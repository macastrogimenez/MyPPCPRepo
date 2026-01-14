package exercises09;

class LongCounter {
    private long count = 0;

    // had to make the methods increment and get synchronized
    public synchronized void increment() { count++; }
    public synchronized long get() { return count; }

    // added these helper functions so that they can be called from the extended class
    protected synchronized void addInternal(long delta) { count += delta; }
    protected synchronized void resetInternal() { count = 0; }

}