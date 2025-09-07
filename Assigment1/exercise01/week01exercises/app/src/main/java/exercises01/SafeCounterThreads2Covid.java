// For week 1
// raup@itu.dk * 2021-08-27
package exercises01;
import java.util.concurrent.locks.ReentrantLock;

public class SafeCounterThreads2Covid {
    public long counter = 0;
    final long PEOPLE  = 10_000;
    final long MAX_PEOPLE_COVID = 15_000;


    public SafeCounterThreads2Covid() {
        try {
            Turnstile turnstile1 = new Turnstile();
            Turnstile turnstile2 = new Turnstile();
            turnstile1.start();turnstile2.start();
            turnstile1.join();turnstile2.join();

            System.out.println(counter+" people entered");
        }
        catch (InterruptedException e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SafeCounterThreads2Covid();
    }

    public class Turnstile extends Thread {
        private ReentrantLock lock = new ReentrantLock();

        @Override
        public void run() {
            for (int i = 0; i < PEOPLE; i++) {
                lock.lock();
                if (counter < MAX_PEOPLE_COVID) {
                    try {
                        counter++;
                    }
                    finally{lock.unlock();}
                }
            }
        }
    }
}


