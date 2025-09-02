package exercises01;

public class UnsafeTestDashBarPrinter {
    private DashBarPrinter p = new DashBarPrinter();
    // boolean starter = true;
    private boolean starter = true;

    public UnsafeTestDashBarPrinter () {
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

        public void print() {
            System.out.print("-");
            try { Thread.sleep(50); } 
            catch (InterruptedException exn) { }
            System.out.print("|");
        }
    }
    
    public static void main(String[] args) {
        new UnsafeTestDashBarPrinter();
    }
    
}