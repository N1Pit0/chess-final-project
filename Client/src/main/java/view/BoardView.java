package view;

import dtos.enums.ImagePath;
import dtos.enums.PieceColor;
import dtos.enums.PieceType;
import lombok.Getter;
import services.board.BoardService;
import services.board.SquareInterface;
import services.strategy.common.PieceInterface;
import services.utils.ImageReaderUtil;
import services.utils.ImageReaderUtilImpl;
import services.utils.exceptions.ImageNotFoundException;

import javax.swing.*;
import java.awt.*;

import static dtos.enums.PieceColor.BLACK;
import static dtos.enums.PieceColor.WHITE;


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
                final Image i = getImage(currPiece.getPieceType(), currPiece.getPieceColor());
                g.drawImage(i, this.boardService.getCurrX(), this.boardService.getCurrY(), null);
            }
        }
    }


    private Image getImage(PieceType pieceType, PieceColor pieceColor) {
        ImageReaderUtil imageReader = new ImageReaderUtilImpl();

        String imgFile;
        switch (pieceType) {
            case PAWN -> imgFile = (pieceColor == BLACK)
                    ? ImagePath.RESOURCES_BPAWN_PNG.label
                    : ImagePath.RESOURCES_WPAWN_PNG.label;
            case KNIGHT -> imgFile = (pieceColor == BLACK)
                    ? ImagePath.RESOURCES_BKNIGHT_PNG.label
                    : ImagePath.RESOURCES_WKNIGHT_PNG.label;
            case BISHOP -> imgFile = (pieceColor == BLACK)
                    ? ImagePath.RESOURCES_BBISHOP_PNG.label
                    : ImagePath.RESOURCES_WBISHOP_PNG.label;
            case ROOK -> imgFile = (pieceColor == BLACK)
                    ? ImagePath.RESOURCES_BROOK_PNG.label
                    : ImagePath.RESOURCES_WROOK_PNG.label;
            case QUEEN -> imgFile = (pieceColor == BLACK)
                    ? ImagePath.RESOURCES_BQUEEN_PNG.label
                    : ImagePath.RESOURCES_WQUEEN_PNG.label;
            case KING -> imgFile = (pieceColor == BLACK)
                    ? ImagePath.RESOURCES_BKING_PNG.label
                    : ImagePath.RESOURCES_WKING_PNG.label;
            default -> throw new IllegalArgumentException("Invalid piece type: " + pieceType);
        }

        return imageReader.readImage(imgFile)
                .orElseThrow(() -> new ImageNotFoundException("Image not found for: " + imgFile));
    }

}
