package client;

import client.interfaces.MoveSender;
import client.interfaces.PgnExporter;
import client.service.PgnExporterImpl;
import shared.dtos.BoardState;
import shared.dtos.GameInit;
import shared.dtos.GameStatus;
import shared.dtos.PgnContent;

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
    private PgnExporter pgnExporter;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Consumer<GameInit> pieceColorListener;
    private volatile boolean running = true;

    private BoardState boardState;

    public ServerConnection(String host, int port, Consumer<BoardState> boardStateListener, Consumer<GameStatus> gameStatusListener, Consumer<GameInit> playerColorListener) {
        this.host = host;
        this.port = port;
        this.boardStateListener = boardStateListener;
        this.gameStatusListener = gameStatusListener;
        this.pieceColorListener = playerColorListener;
//        this.pgnExporter = new PgnExporterImpl(in, out);
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            this.pgnExporter = new PgnExporterImpl(in, out);

            boolean isGameStatusReceived = false;
            while (running) {
                Object object = in.readObject();
                if (object instanceof BoardState state) {
                    boardState = state;
                    boardStateListener.accept(state);
                    if (state.getGameStatus() != null) {
                        Thread.sleep(1000);
                        if (!isGameStatusReceived) {
                            gameStatusListener.accept(state.getGameStatus());
                            isGameStatusReceived = true;
                        }
                        //                        closeConnection();
                    }
                }

                if (object instanceof GameInit) {
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

    public synchronized PgnContent GetPgnContent() throws IOException {
        return boardState.getPgnContent();
    }

    public void closeConnection() {
        running = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException ignored) {
        }
    }
}
