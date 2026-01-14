package exercises09;
class PrimeCounter {
  private int count= 0;
  public synchronized void increment() {
    count= count + 1;
  }
  public synchronized int get() { 
    return count; 
  }
  public synchronized void add(int c) {
    // used LongCounter and ExtendedLongCounter instead since I was unsure how to 
    // understand the phrasing of exercise 9.4
  }
  public synchronized void reset() {
    // used LongCounter and ExtendedLongCounter instead since I was unsure how to 
    // understand the phrasing of exercise 9.4
  }
}