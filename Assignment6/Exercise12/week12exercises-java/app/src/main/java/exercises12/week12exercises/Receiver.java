// Demonstration of MessageReceiver
// Receives messages from Sender via UDP
// jst@itu.dk * 2025-07-10
package week12exercises;

public class Receiver {

  private MessageReceiver mr;
  private final int port= 8080;

  public static void main(String[] args) {
    new Receiver();
  }

  public Receiver(){
    mr= new MessageReceiver(port);
    String m= "stop";
    do {
      m= mr.receiveMessage();
      System.out.println(m);
    } while (!m.equals("stop"));
    mr.stop();
  }
}