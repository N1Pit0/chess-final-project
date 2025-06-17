package client;

import client.MoveSender;
import dtos.BoardState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerConnection implements Runnable, MoveSender {
    private final String host;
    private final int port;
    private final Consumer<BoardState> boardStateListener;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private volatile boolean running = true;

    public ServerConnection(String host, int port, Consumer<BoardState> boardStateListener) {
        this.host = host;
        this.port = port;
        this.boardStateListener = boardStateListener;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (running) {
                var object = in.readObject();
                if(object instanceof BoardState) {
                    BoardState state = (BoardState) in.readObject();
                    boardStateListener.accept(state);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connection lost: " + e.getMessage());
        } finally {
//            closeConnection();
        }
    }

    @Override
    public synchronized void sendMove(String move) throws IOException {
        if (out != null) {
            out.writeObject(move);
            out.flush();
        } else {
            throw new IOException("Output stream is not initialized.");
        }
    }

    public void closeConnection() {
        running = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
}
