package view;

import lombok.Getter;
import lombok.Setter;
import services.board.SquareInterface;

import javax.swing.*;
import java.awt.*;

import static dtos.enums.PieceColor.WHITE;


@Getter
@Setter
public class SquareView extends JComponent {
    private SquareInterface square;
    private boolean displayPiece;

    public SquareView(SquareInterface square) {
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

        PieceView pieceView = new PieceView();

        if (this.square.getOccupyingPiece() != null && displayPiece) {
            pieceView.draw(g, square.getOccupyingPiece());
        }
    }
}
