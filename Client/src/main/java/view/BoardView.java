package view;

import lombok.Getter;
import services.board.BoardService;
import services.board.SquareInterface;
import services.enums.PieceColor;
import services.strategy.common.PieceInterface;

import javax.swing.*;
import java.awt.*;

import static services.enums.PieceColor.BLACK;
import static services.enums.PieceColor.WHITE;

@Getter
public class BoardView extends JPanel {
    private final BoardService boardService;

    public BoardView(BoardService boardService) {
        this.boardService = boardService;

        setLayout(new GridLayout(8, 8, 0, 0));

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                this.add(new SquareView(this.boardService.getBoardSquareArray()[x][y])); // ??
            }
        }

        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        SquareInterface[][] board = this.boardService.getBoardSquareArray();
        PieceInterface currPiece = this.boardService.getCurrPiece();
        boolean whiteTurn = this.boardService.isWhiteTurn();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                SquareInterface sq = board[y][x];
                SquareView sqView = new SquareView(sq);
                sqView.setDisplayPiece(true);
                sqView.paintComponent(g);
            }
        }

        if (currPiece != null) {
            PieceColor pieceColor = currPiece.getPieceColor();
            if ((pieceColor.equals(WHITE) && whiteTurn)
                    || (pieceColor.equals(BLACK) && !whiteTurn)) {
                final Image i = currPiece.getImage();
                g.drawImage(i, this.boardService.getCurrX(), this.boardService.getCurrY(), null);
            }
        }
    }
}
