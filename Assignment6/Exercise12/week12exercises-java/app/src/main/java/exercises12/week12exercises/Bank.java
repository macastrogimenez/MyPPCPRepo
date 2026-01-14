// Bank for exercise 1 
// Each account has an address (port number) given in the constructor
// jst@itu.dk * 2025-07-11
package week12exercises;

public class Bank{

  //To recieve messages from MobileApps
  private MessageReceiver mr;
  private final int port= 8080;

  //To send messages to Accounts
  private String host= "localhost";
  private MessageSender toAccount;
 
  private Convert convert;

  private int a1, a2, amount;

  public static void main(String[] args) {
    new Bank();
  }

  public Bank(){
    convert= new Convert();
    toAccount= new MessageSender("localhost");
    mr= new MessageReceiver(port);
    String m= "stop";
    do {
      m= mr.receiveMessage();
      System.out.println(m);
      // m is encoded as {a1, a2, amount}
      a1= convert.getA1(m);
      a2= convert.getA2(m);
      amount= convert.getAmount(m);
      toAccount.sendMessage(Integer.toString(-amount), a1);
      toAccount.sendMessage(Integer.toString(amount), a2);
      //System.out.println(m);
    } while (amount != 0);
    mr.stop();
  }
}