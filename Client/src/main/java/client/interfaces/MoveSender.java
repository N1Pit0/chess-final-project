package client.interfaces;

import java.io.IOException;

public interface MoveSender {
    void sendMove(String move) throws IOException;
}