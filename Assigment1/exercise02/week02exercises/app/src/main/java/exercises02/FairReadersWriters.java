package exercises02;

public class FairReadersWriters{
    private int sharedResource;

    public int getSharedResource() {
        return sharedResource;
    }

    public class ReadWriteMonitor {
        private int readers         = 0;
        private boolean writer      = false;

        //////////////////////////
        // Read lock operations //
        //////////////////////////

        public synchronized void readLock() throws InterruptedException{
            while(writer)
                this.wait();
            readers++;
        }

        public synchronized void readUnlock() {
            readers--;
            if(readers==0)
                this.notifyAll();
        }


        ///////////////////////////
        // Write lock operations //
        ///////////////////////////

        public synchronized void writeLock() throws InterruptedException {
            while (writer) // this method has been edited to ensure fairness
                this.wait();
            writer=true; 
            while(readers > 0)
                this.wait();   
        }

        public synchronized void writeUnlock() {
            writer=false;
            this.notifyAll();
        }
    }

    public FairReadersWriters() {
        ReadWriteMonitor m = new ReadWriteMonitor();
        this.sharedResource = 0;
        final int numReadersWriters = 10;

        for (int i = 0; i < numReadersWriters; i++) {

            // start a reader
            new Thread(() -> {
                try {
                    m.readLock();
                    System.out.println(" Reader " + Thread.currentThread().getId() + " started reading");
                    int currentValueSR = this.sharedResource;
                    System.out.println(" Reader " + Thread.currentThread().getId() + " sharedResource is currently at "+currentValueSR);
                    System.out.println(" Reader " + Thread.currentThread().getId() + " stopped reading");
                    m.readUnlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                    
            }).start();

            // start a writer
            new Thread(() -> {
                try {
                    m.writeLock();
                    System.out.println(" Writer " + Thread.currentThread().getId() + " started writing");
                    if (sharedResource<100) {
                            sharedResource++;
                            System.out.println("count: "+sharedResource);
                    }
                    System.out.println(" Writer " + Thread.currentThread().getId() + " stopped writing");
                    m.writeUnlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                    
            }).start();

        }
    }

    public static void main(String[] args) {
        new FairReadersWriters();
    }

}
