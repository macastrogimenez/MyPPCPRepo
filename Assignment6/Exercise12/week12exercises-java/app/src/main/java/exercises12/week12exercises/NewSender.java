// Demonstration of MessageSender
// Sends to Receiver via UDP
// jst@itu.dk * 2025-07-17
package week12exercises;

public class NewSender {

  private MessageSender ms;
  private int port= 8080;
  private String host= "localhost";

  public static void main(String[] args) {
      new NewSender();
  }

  public NewSender(){
    ms= new MessageSender(host);
    ms.sendMessage("HELLO", port);
    ms.sendMessage("WORLD", port);
    ms.sendMessage("!", port);
    ms.sendMessage(".", port);
    //ms.sendMessage("stop", port);
  }
}