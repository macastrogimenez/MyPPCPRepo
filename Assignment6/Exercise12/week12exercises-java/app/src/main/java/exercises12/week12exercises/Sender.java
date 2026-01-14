// Demonstration of MessageSender
// Sends to Receiver via UDP
// jst@itu.dk * 2025-07-17
package week12exercises;

public class Sender {

  private MessageSender ms;
  private int port= 8080;
  private String host= "localhost";

  public static void main(String[] args) {
      new Sender();
  }

  public Sender(){
    ms= new MessageSender(host);
    ms.sendMessage("hello", port);
    ms.sendMessage("world", port);
    ms.sendMessage("!", port);
    ms.sendMessage(".", port);
    //ms.sendMessage("stop", port);
  }
}