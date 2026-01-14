package exercises09;

public class TestVolatile {
    private volatile int vCtr;
    private int ctr;
    
    public void vInc() { vCtr++; }
    public void inc() { ctr++; }
}