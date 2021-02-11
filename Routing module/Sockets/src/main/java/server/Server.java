package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void startServer() {
        /*...*/
    }

    public static void main(String[] args) {
        try {
            try {
                // Необходимо определиться с портом клиента и сервера.
                // Максимум подключений = 1.
                // TODO: Порт от Вани.
                // TODO: Порт получения.
                // TODO: Порт отправки.
                // clientSocket.getPort(); - в зависимости от него выстраивать логику.
                serverSocket = new ServerSocket(8000, 1);

                // Запуск сервера и ожидание подключения к нему.
                System.out.println("Server started.");
                clientSocket = serverSocket.accept();

                try {
                    // Получение потоков вводаю/вывода клиентского сокета, по которым отправляются/получаются сообщения.
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    while (true) {
                        // Получение сообщения от клиента.
                        String inputData = in.readLine();

                        // Извлечение из сообщения идентификатора типа сообщения.
                        // Длина идентификатора типа сообщения = 4 (например, 0100).
                        String messageTypeID = inputData.substring(0, 4);

                        System.out.println("Message type ID: " + messageTypeID);

                        if (false/*Не распаршено ли сообщение?*/) {
                            /*...*/
                        } else {
                            // Ответ на сообщение.
                            if (messageTypeID.equals("0100")) {
                                out.write("Request 0100.\n");
                            } else if (messageTypeID.equals("0110")) {
                                out.write("Response 0110.\n");
                            } else {
                                out.write("Unhandled message.\n");
                            }
                        }
                        out.flush();
                    }
                } finally {
                    clientSocket.close();
                    in.close();
                    out.close();
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
