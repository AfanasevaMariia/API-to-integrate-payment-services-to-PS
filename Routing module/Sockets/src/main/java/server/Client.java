package server;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static Socket clientSocket;
    // Ридер консоли (временно).
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void startClient() {
        /*...*/
    }

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 8000);

                // Пломба: принятие данных из другого модуля.
                reader = new BufferedReader(new InputStreamReader(System.in));
                // Пломба: принятие данных из другого модуля.

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                while (true) {
                    System.out.println("Enter your message for server: ");
                    String inputData = reader.readLine();

                    // Отправка сообщения на сервер.
                    // Без "\n" сообщение не отправится.
                    out.write(inputData + "\n");
                    out.flush();

                    String serverAnswer = in.readLine();
                    System.out.println("Server answered: " + serverAnswer);
                }
            } finally {
                clientSocket.close();
                in.close();
                out.close();
                System.out.println("Client closed.");
            }
        } catch (UnknownHostException ex) {

        } catch (IOException ex) {

        }
    }
}
