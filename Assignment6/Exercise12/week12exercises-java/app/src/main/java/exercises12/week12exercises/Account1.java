// Account for exercise 1 
// Each account has an address (port number) given in the constructor
// jst@itu.dk * 2025-07-11
package week12exercises;

public class Account1 {
    private MessageReceiver mr;
    private final int port = 8081;
    private int balance = 0;
    int amount = 0;
    private Convert convert;

    public static void main(String[] args) {
        new Account1();
    }

    public Account1() {
        mr = new MessageReceiver(port);
        convert = new Convert();
        String m = "started";
        do {
            m = mr.receiveMessage();
            amount = Integer.parseInt(m);
            System.out.println("Mes: " + m + " Amount; " + amount);
            balance = balance + amount;
            System.out.println("Account1; " + balance);
        } while (amount != 0); // sending a 0 amount means stop
    }
}