package client;

import shared.dtos.BoardState;
import shared.dtos.GameInit;
import shared.dtos.GameStatus;
import shared.enums.PieceColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerConnection implements Runnable, MoveSender {
    private final String host;
    private final int port;
    private final Consumer<BoardState> boardStateListener;
    private final Consumer<GameStatus> gameStatusListener;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Consumer<GameInit> pieceColorListener;
    private volatile boolean running = true;

    public ServerConnection(String host, int port, Consumer<BoardState> boardStateListener, Consumer<GameStatus> gameStatusListener, Consumer<GameInit> playerColorListener) {
        this.host = host;
        this.port = port;
        this.boardStateListener = boardStateListener;
        this.gameStatusListener = gameStatusListener;
        this.pieceColorListener = playerColorListener;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (running) {
                Object object = in.readObject();
                if(object instanceof BoardState state) {
                    boardStateListener.accept(state);
                    if(state.getGameStatus() != null){
                        Thread.sleep(1000);
                        gameStatusListener.accept(state.getGameStatus());
//                        closeConnection();
                    }
                }

                if(object instanceof GameInit) {
                    pieceColorListener.accept((GameInit) object);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connection lost: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
