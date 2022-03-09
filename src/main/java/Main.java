import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        System.out.println(engine.search("бизнес"));
        int port = 8989;

        while (true) {
            try (
                    ServerSocket serverSocket = new ServerSocket(port);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {
                System.out.printf("New connection accepted. Port: %d%n", clientSocket.getPort());
                String input = in.readLine();
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                out.println(gson.toJson(engine.search(input)));
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}