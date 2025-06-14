package view;

import services.strategy.common.PieceInterface;

import java.awt.*;

public class PieceView {

    public void draw(Graphics g, PieceInterface piece) {


        g.drawImage(piece.getImage(), 0, 0, null);
    }
}
