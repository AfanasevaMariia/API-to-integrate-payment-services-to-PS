package mir.routing.emulator;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import mir.routing.constants.Constants;
import mir.routing.exception.PortNotFoundException;

import java.io.*;
import java.net.*;


public class IssuerModule {
    private final static String PAYLOAD_HEADER = "Payload";
    private static int port;

    private static int getSendPort(int mti) throws PortNotFoundException {
        switch (port) {
            case Constants.Ports.ACQUIRER_MODULE:
            case Constants.Ports.ISSUER_MODULE:
                return Constants.Ports.PLATFORM_MODULE;
            case Constants.Ports.PLATFORM_MODULE:
                if (mti == 0100) {
                    return Constants.Ports.ISSUER_MODULE;
                } else /*if (mti == 0110)*/ {
                    return Constants.Ports.ACQUIRER_MODULE;
                }
            default:
                // TODO: Change to Exception type, not RuntimeException.
                throw new PortNotFoundException("There's no module with port provided");
        }
    }

    private static void handleGetRequest(HttpExchange exchange) throws IOException {
        String respText;
        OutputStream output;

        Headers headers = exchange.getRequestHeaders();

        if (headers.containsKey(PAYLOAD_HEADER)) {
            // --- CORRECT HTTP REQUEST --- //
            String payloadContent = headers.getFirst(PAYLOAD_HEADER);

            if (true/*CheckIfCorrect(payloadContent)*/) { // TODO: Модуль проверки сообщений.
                respText = payloadContent + " from issuer";

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

    public static void main(String[] args) {
        IssuerModule issuerModule = new IssuerModule(Constants.Ports.ISSUER_MODULE);
        issuerModule.start();
    }

    public IssuerModule(int port) {
        this.port = port;
    }
}
