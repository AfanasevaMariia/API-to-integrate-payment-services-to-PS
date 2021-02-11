package src.main.java.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Реализация общения двух серверов друг с другом (допустим, это Parsing Module).
 */
public class AnotherServer {
    private static Socket clientSocket, sendSocket;
    private static ServerSocket serverSocket;
    private static BufferedReader inClient, inSend;
    private static BufferedWriter outClient, outSend;

    public static void main(String[] args) {
        try {
            try {
                // Порт отправки сообщенний в Parsing Module = 8011.
                serverSocket = new ServerSocket(8011, 1);

                // Запуск сервера и ожидание подключения к нему.
                System.out.println("Server started.");
                clientSocket = serverSocket.accept();

                try {
                    // Получение потоков вводаю/вывода клиентского сокета, по которым отправляются/получаются сообщения.
                    inClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    outClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    System.out.println("Port: " + clientSocket.getPort() + "\n" +
                            "Local port: " + clientSocket.getLocalPort());
                    // Получение сообщения от клиента.
                    String inputData = inClient.readLine();

                    System.out.println(inputData);

                    // Отправка сообщений в Routing Module.
                    sendSocket = new Socket("localhost", 8012);
                    inSend = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
                    outSend = new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream()));

                    outSend.write(inputData);
                    outSend.flush();
                } finally {
                    clientSocket.close();
                    inClient.close();
                    outClient.close();
                }
            } finally {
                serverSocket.close();
                System.out.println("Server closed.");
            }
        } catch (IOException ex) {
            // TODO
            System.out.println("Как-то отреагировать на ошибку.");
        }
    }
}
