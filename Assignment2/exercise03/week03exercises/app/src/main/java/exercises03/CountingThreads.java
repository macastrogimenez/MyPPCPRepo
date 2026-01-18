// For week 3 
// raup@itu.dk * 2025-09-03

package exercises03;

public class CountingThreads {
    int count; // 0

    public CountingThreads() throws InterruptedException {
        count = 0; // 1
        
        CountingThread t1 = new CountingThread(); // 2
        CountingThread t2 = new CountingThread(); // 3
        
        t1.start(); // 4
        t2.start(); // 5

        t1.join(); // 6
        t2.join(); // 7
        
        System.out.println("count="+count); // 8
    }

    public class CountingThread extends Thread {
        public void run() {
            int temp = count; // 9
            count = temp + 1; // 10       
        }
    }


    public static void main(String[] args) throws InterruptedException {
        new CountingThreads(); // 11
    }
}
