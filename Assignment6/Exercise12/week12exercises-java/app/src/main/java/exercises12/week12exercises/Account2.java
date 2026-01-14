// Account2 for exercise 1 
package week12exercises;

public class Account2 {
    private MessageReceiver mr;
    private final int port = 8082;
    private int balance = 0;
    int amount = 0;
    private Convert convert;

    public static void main(String[] args) {
        new Account2();
    }

    public Account2() {
        mr = new MessageReceiver(port);
        convert = new Convert();
        String m = "started";
        do {
            m = mr.receiveMessage();
            amount = Integer.parseInt(m);
            System.out.println("Mes: " + m + " Amount; " + amount);
            balance = balance + amount;
            System.out.println("Account2; " + balance);
        } while (amount != 0); // sending a 0 amount means stop
    }
}