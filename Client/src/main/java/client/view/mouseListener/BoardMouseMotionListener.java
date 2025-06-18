package client.view.mouseListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class BoardMouseMotionListener extends MouseMotionAdapter {

    private final CustomBoardMouseListener mouseListener;

    public BoardMouseMotionListener(CustomBoardMouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseListener.handleMouseDragged(e);
    }

}
