// Server class to receive on a port set in Constructor
// e.g. new MessageReceiver(4445)
// jst@itu.dk * 2025-07-10
package week12exercises;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MessageReceiver {
  private DatagramSocket socket;
  private byte[] buf= new byte[256];

  public MessageReceiver(int port) {
    try {
      socket= new DatagramSocket(port);
    } catch (IOException e) { e.printStackTrace(); }
  }

  public String receiveMessage() {
    String result= "E";
    try {
      DatagramPacket packet= new DatagramPacket(buf, buf.length);
      socket.receive(packet);
      result= new String(packet.getData(), 0, packet.getLength());
    } catch (IOException e) { e.printStackTrace(); }
    return result;
  }

  public void stop() {
    //System.out.println("Stopping");
    socket.close();
  }
}