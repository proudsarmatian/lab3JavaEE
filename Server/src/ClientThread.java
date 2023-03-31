import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {

    Socket clientSocket;
    ChatServer chatServer;
    int numberClient;

    public ClientThread(Socket clientSocket, ChatServer chatServer, int number) {
        this.clientSocket = clientSocket;
        this.chatServer = chatServer;
        this.numberClient = number;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Client num: " + numberClient + " connected.");
            new PrintWriter(clientSocket.getOutputStream(), true).println("Client num" + numberClient + ".");
            String clientMessage = null;

            while (true) {
                clientMessage = in.readLine();
                if (!"exit".equals(clientMessage)) {
                    System.out.println("Client num" + numberClient + ": " + clientMessage);
                    chatServer.sendMessageForAllClient(numberClient, clientMessage);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
