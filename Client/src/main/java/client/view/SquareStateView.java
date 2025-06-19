package client.view;

import shared.dtos.SquareState;
import lombok.Getter;
import lombok.Setter;
import shared.enums.PieceColor;

import javax.swing.*;
import java.awt.*;

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

        if (this.square.getSquareColor().equals(PieceColor.WHITE)) {
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
