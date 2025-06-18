package client;

import client.view.gui.StartMenu;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new StartMenu());
    }
}
