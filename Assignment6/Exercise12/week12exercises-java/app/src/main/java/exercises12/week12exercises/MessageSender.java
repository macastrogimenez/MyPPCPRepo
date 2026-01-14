// Client class to send messages to a Server on computer
// jst@itu.dk * 2025-13-10
package week12exercises;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class MessageSender {
  private DatagramSocket clientSocket;

  private InetAddress address;
  private byte[] buf;

  public MessageSender(String addr) {
    try {
      clientSocket= new DatagramSocket();
      address= InetAddress.getByName(addr);
    } catch (IOException e) { e.printStackTrace(); }
  }

  public void sendMessage(String msg, int port) {
    try {
      buf= msg.getBytes();
      DatagramPacket packet= 
        new DatagramPacket(buf, buf.length, address, port);
      clientSocket.send(packet);
    } catch (IOException e) { e.printStackTrace(); }
  }

  public void stopConnection() {
    clientSocket.close();
  }
}  