package mir.routing;

//import mir.routing.exception.UnsupportedMessageTypeIDException;

import mir.routing.constants.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
@EntityScan(basePackages = "mir.models")
@ComponentScan(basePackages = "mir")
@EnableJpaRepositories(basePackages = "mir.repositories")
public class Application {
//    private static final int MTI_LENGTH = 4;

    static public void start() {

    }

    static public String sendMessage(String message) {
        String answer = null;
        try {
            Socket sendSocket = new Socket("localhost", Constants.Ports.ACQUIRER_MODULE);
            ServerSocket serverSocket = new ServerSocket(Constants.Ports.ROUTING_MODULE, 1);

            DataOutputStream out = new DataOutputStream(sendSocket.getOutputStream());

            out.writeUTF(message);
            out.flush();

            Socket acquirerAnswerSocket = serverSocket.accept();
            DataInputStream in = new DataInputStream(acquirerAnswerSocket.getInputStream());

            answer = in.readUTF();

            return answer;
        } catch (IOException ex) {
            // TODO: как-то отреагировать.
        }

        return answer;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        // TODO: сделать запуск модулей эквайера, платформы и эмитента параллельным, вызовом одной функции.
        // TODO: На данный момент каждый модуль надо запускать самому.
        // run();

        System.out.println(sendMessage("asdasd)"));
    }
}
