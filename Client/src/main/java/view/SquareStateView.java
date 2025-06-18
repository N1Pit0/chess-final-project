package view;

import dtos.SquareState;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

import static enums.PieceColor.WHITE;
@Getter
@Setter
public class SquareStateView extends JComponent{

    private SquareState square;
    private boolean displayPiece;

    public SquareStateView(SquareState square) {
        this.square = square;
        this.displayPiece = true;

        this.setBorder(BorderFactory.createEmptyBorder());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.square.getSquareColor().equals(WHITE)) {
            g.setColor(new Color(221, 192, 127));
        } else {
            g.setColor(new Color(101, 67, 33));
        }

        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        PieceStateView pieceStateView = new PieceStateView();

        if (this.square.getOccupyingPiece() != null && displayPiece) {
            pieceStateView.draw(g, square.getOccupyingPiece());
        }
    }


}
