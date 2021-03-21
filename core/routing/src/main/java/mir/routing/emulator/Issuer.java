package mir.routing.emulator;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.*;

import static mir.routing.constants.Constants.Headers.PAYLOAD_HEADER;

public class Issuer {
    private static int port;

    private static void handleGetRequest(HttpExchange exchange) throws IOException {
        String respText;
        OutputStream output;

        Headers headers = exchange.getRequestHeaders();

        if (headers.containsKey(PAYLOAD_HEADER)) {
            // --- CORRECT HTTP REQUEST --- //
            String payloadContent = headers.getFirst(PAYLOAD_HEADER);

            if (true/*CheckIfCorrect(payloadContent)*/) { // TODO: Модуль проверки сообщений.
                respText = payloadContent; // TODO: Parsing.

                exchange.sendResponseHeaders(200, respText.getBytes().length);
                output = exchange.getResponseBody();
            } else {
                // --- INCORRECT HEADER CONTENT --- //
                respText = String.format("Incorrect \"%s\" header content format.\n", PAYLOAD_HEADER);

                exchange.sendResponseHeaders(422, respText.getBytes().length);
                output = exchange.getResponseBody();
            }
        } else {
            // --- NO HEADER IN HTTP REQUEST --- //
            respText = String.format("No \"%s\" header found in the request. Add header to get response.\n", PAYLOAD_HEADER);

            exchange.sendResponseHeaders(400, respText.getBytes().length);
            output = exchange.getResponseBody();
        }


        output.write(respText.getBytes());
        output.flush();
    }

    private static void handleWrongRequest(HttpExchange exchange) throws IOException {
        // 405 Method Not Allowed.
        exchange.sendResponseHeaders(405, -1);
    }


    public static void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext("/api", (exchange -> {
                if ("GET".equals(exchange.getRequestMethod())) {
                    handleGetRequest(exchange);
                } else {
                    handleWrongRequest(exchange);
                }
                exchange.close();
            }));

            // Creates a default executor.
            server.setExecutor(null);
            server.start();
        } catch (IOException ex) {
            // TODO: отреагировать по-нормальному.
            System.out.println("Некая какая-то проблема на стороне сервера.");
            System.out.println(ex.getMessage());
        }
    }

    public Issuer(int port) {
        this.port = port;
    }
}
