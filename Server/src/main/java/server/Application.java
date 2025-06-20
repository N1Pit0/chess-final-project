package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import server.repositories.MatchRepository;
import server.repositories.UserRepository;
import server.socket.ChessServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {UserRepository.class, MatchRepository.class })
public class Application implements CommandLineRunner {
    private ChessServer server;

    @Autowired
    public void setServer(ChessServer server) {
        this.server = server;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        int port = 9999;
        ExecutorService executor = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chess server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                executor.submit(() -> server.handleNewClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}