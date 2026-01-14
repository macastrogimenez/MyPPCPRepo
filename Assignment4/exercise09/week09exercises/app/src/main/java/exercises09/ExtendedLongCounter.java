package exercises09;
class ExtendedLongCounter extends LongCounter{

  public void add(int c) {
    addInternal(c);
  }

  public void reset() {
    resetInternal();
  }
}