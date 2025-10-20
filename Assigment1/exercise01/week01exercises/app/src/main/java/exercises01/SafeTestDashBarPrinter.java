package exercises01;

import java.util.concurrent.locks.ReentrantLock;

public class SafeTestDashBarPrinter {
    private DashBarPrinter p = new DashBarPrinter();
    private boolean starter = true;

    public SafeTestDashBarPrinter () {
        Thread t1 = new Thread(()-> {
            while (starter) {
                p.print();
            }
        });

        Thread t2 = new Thread(()-> {
            while (starter) {
                p.print();
            }
        });

        t1.start(); t2.start();
        try {t1.join(); t2.join();}
        catch (InterruptedException exn) {
            System.out.println("Some thread was interrupted");
        }
    }


    public class DashBarPrinter {
        ReentrantLock lock = new ReentrantLock(); // new line for Thread-safety

        public void print() {
            lock.lock(); // new line for Thread-safety
            try{
                System.out.print("-");
                try { Thread.sleep(50); } 
                catch (InterruptedException exn) { }
                System.out.print("|");
            }
            finally{
                lock.unlock(); // new line for Thread-safety
            }
        }
    }
    
    public static void main(String[] args) {
        new SafeTestDashBarPrinter();
    }
    
}

