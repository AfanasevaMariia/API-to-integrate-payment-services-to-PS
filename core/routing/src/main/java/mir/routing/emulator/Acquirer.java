package mir.routing.emulator;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import mir.routing.constants.Constants;
import mir.routing.exception.PortNotFoundException;

import java.io.*;
import java.net.*;

public class AcquirerModule {
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

    private static String sendHttpRequest(int sendPort, String payloadContent) throws IOException {
        // Configure request.
        URL url = new URL(String.format("http://localhost:%d/api", sendPort)); // TODO: getPort(mti);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type", "text/plain");
        connection.setRequestProperty("Content-Length", Integer.toString(payloadContent.getBytes().length));
        connection.setRequestProperty(PAYLOAD_HEADER, payloadContent);

        // Send request.
        InputStream responseStream = connection.getInputStream();

        // Get response.
        BufferedReader in = new BufferedReader(new InputStreamReader(responseStream));
        StringBuffer response = new StringBuffer();
        String inLine;
        while ((inLine = in.readLine()) != null) {
            response.append(inLine);
        }
        in.close();

        return String.format("%s", response.toString());
    }

    private static void handleGetRequest(HttpExchange exchange) throws IOException {
        String respText;
        OutputStream output;

        Headers headers = exchange.getRequestHeaders(); /*"No headers found in the request. Add header to get response.\n"*/;

        if (headers.containsKey(PAYLOAD_HEADER)) {
            // --- CORRECT HTTP REQUEST --- //
            String payloadContent = headers.getFirst(PAYLOAD_HEADER);

            if (true/*CheckIfCorrect(payloadContent)*/) { // TODO: Модуль проверки сообщений.
                // --- CORRECT HEADER CONTENT --- //
                respText = sendHttpRequest(Constants.Ports.PLATFORM_MODULE, payloadContent + " from acquirer");

                respText += " from acquirer\n";

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
        AcquirerModule acquirerModule = new AcquirerModule(Constants.Ports.ACQUIRER_MODULE);
        acquirerModule.start();
    }

    public AcquirerModule(int port) {
        this.port = port;
    }
}
