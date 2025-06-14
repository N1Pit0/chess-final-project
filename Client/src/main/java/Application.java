import view.gui.StartMenu;

import javax.swing.*;

public class Application implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Application());
    }

    public void run() {
        SwingUtilities.invokeLater(new StartMenu());
    }
}
