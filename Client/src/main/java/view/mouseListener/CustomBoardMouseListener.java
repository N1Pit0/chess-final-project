package view.mouseListener;

import java.awt.event.MouseEvent;

public interface CustomBoardMouseListener {

    void handleMouseReleased(MouseEvent e);

    void handleMousePressed(MouseEvent e);

    void handleMouseDragged(MouseEvent e);
}
