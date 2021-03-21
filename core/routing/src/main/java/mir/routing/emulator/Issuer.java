package mir.routing.emulator;

import com.imohsenb.ISO8583.exceptions.ISOException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import mir.models.ParsedMessage;
import mir.parsing.routing.Router;

import java.io.*;
import java.net.*;

import static mir.routing.constants.Constants.Headers.PAYLOAD_HEADER;
import static mir.routing.constants.Constants.Ports.ISSUER_MODULE;

public class Issuer {
    private static int port;

    private static void handleGetRequest(HttpExchange exchange) throws IOException, ISOException {
        String respText;
        OutputStream output;

        Headers headers = exchange.getRequestHeaders();

        if (headers.containsKey(PAYLOAD_HEADER)) {
            // --- CORRECT HTTP REQUEST --- //
            String payloadContent = headers.getFirst(PAYLOAD_HEADER);

            ParsedMessage parsedMessage = Router.getParsedMessage(payloadContent);
            ParsedMessage parsedMessage1 = parsedMessage; // TODO: модуль формирования сообщения.

            respText = Router.getEncodedMessage(parsedMessage1);

            exchange.sendResponseHeaders(200, respText.getBytes().length);
            output = exchange.getResponseBody();
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
                    try {
                        handleGetRequest(exchange);
                    } catch (ISOException e) {
                        // TODO: обработать.
                    }
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
