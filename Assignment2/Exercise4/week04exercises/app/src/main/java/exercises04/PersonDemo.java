package exercises04;

public class PersonDemo {
    public static void run(String[] args) throws InterruptedException {
        var t1 = new Thread(() -> {
            var p1 = new Person();
            System.out.println("p1 id: " + p1.getId());
        });
        
        var t2 = new Thread(() -> {
            var p2 = new Person(11110);
            System.out.println("p2 id: " + p2.getId());
        });

        var t3 = new Thread(() -> {
            var p3 = new Person();
            System.out.println("p3 id: " + p3.getId());
        });

        var t4 = new Thread(() -> {
            var p4 = new Person();
            System.out.println("p4 id: " + p4.getId());
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
    }
}
