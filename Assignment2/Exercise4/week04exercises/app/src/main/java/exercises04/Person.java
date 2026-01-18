package exercises04;

public class Person {
    private static volatile long uuid = 0;
    private static volatile boolean isFirst = true;

    private final long id;
    private String name;
    private int zip;
    private String address;
    
    public Person() {
        synchronized(Person.class) {
            this.id = Person.uuid;
            Person.uuid += 1;
            Person.isFirst = false;
        }
    }

    public Person(long id) {
        synchronized(Person.class) {
            if (isFirst) {
                Person.uuid = id;
                Person.isFirst = false;
            }
            this.id = Person.uuid;
            Person.uuid += 1;
        }
    } 

    public synchronized void updateZipAndAddress(String address, int zip) {
        this.zip = zip;
        this.address = address;
    }

    public synchronized long getId() {
        return id;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized int getZip() {
        return zip;
    }

    public synchronized String getAddress() {
        return address;
    }
}

