package client.view.mouseListener;

import java.awt.event.MouseEvent;
import java.io.IOException;

public interface CustomBoardMouseListener {

    void handleMouseReleased(MouseEvent e) throws IOException;

    void handleMousePressed(MouseEvent e);

    void handleMouseDragged(MouseEvent e);
}
