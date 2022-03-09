import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (
                Socket socket = new Socket("localhost", 8989);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            System.out.println("Введите слово для поиска:");
            String word = scanner.nextLine();
            out.println(word);
            System.out.println(in.readLine());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}


