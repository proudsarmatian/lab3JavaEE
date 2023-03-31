import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

public class ChatServer implements Runnable {

    private Map<Integer, Socket> mapClient = new TreeMap<>();

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(8887);
            System.out.println("Server is running. Waiting for clients. ");
            int numberClient = 1;
            Socket client;

            while (true) {
                client = server.accept();
                Thread clientThread = new Thread(new ClientThread(client, this, numberClient));
                clientThread.setDaemon(true);
                clientThread.start();
                mapClient.put(numberClient, client);
                numberClient++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageForAllClient(int numberClient, String clientMessage) {
        for (int i = 1; i < mapClient.size() + 1; i++) {

            if (numberClient != i) {

                System.out.println("Sending message to client num:" + i + '\n');
                BufferedWriter out;

                try {
                    out = new BufferedWriter(new OutputStreamWriter(mapClient.get(i).getOutputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                try {
                    out.write("\nClient num" + numberClient + ": " + clientMessage + "\n");
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }
}