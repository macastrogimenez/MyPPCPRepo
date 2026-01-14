// Mobile App Exercise 1
// jst@itu.dk * 2025-07-11
package week12exercises;

public class MobileApp {
    private int port = 8080;
    private String host = "localhost";

    public static void main(String[] args) {
        new MobileApp();
    }

    public int getPort() {
        return port;
    }

    public MobileApp() {
        MessageSender ms = new MessageSender(host);
        ms.sendMessage("{8081,8082!100}", port);
        ms.sendMessage("{8081,8082!-50}", port);
        ms.sendMessage("{8082,8081!17}", port);
        // Stop bank
        ms.sendMessage("{8081,8082!0}", port);
    }
}