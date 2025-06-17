package view;

import dtos.BoardState;
import dtos.PieceState;
import dtos.SquareState;
import dtos.enums.ImagePath;
import dtos.enums.PieceColor;
import dtos.enums.PieceType;
import lombok.Getter;
import lombok.Setter;
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
@Setter
public class BoardStateView extends JPanel {
    private  BoardState boardState;
    private SquareStateView[][] squareViews = new SquareStateView[8][8];


    public BoardStateView(BoardState boardState) {
        this.boardState = boardState;
        setLayout(new GridLayout(8, 8, 0, 0));

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                SquareStateView sqView = new SquareStateView(boardState.getBoardSquareArray()[x][y]);
                squareViews[x][y] = sqView;
                this.add(sqView);
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
        SquareState[][] board = this.boardState.getBoardSquareArray();
        PieceState currPiece = this.boardState.getCurrPiece();
        boolean whiteTurn = this.boardState.isWhiteTurn();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                SquareState sq = board[y][x];
                SquareStateView sqView = new SquareStateView(sq);
                sqView.setDisplayPiece(true);
                sqView.paintComponent(g);
            }
        }

        if (currPiece != null) {
            PieceColor pieceColor = currPiece.getPieceColor();
            if ((pieceColor.equals(WHITE) && whiteTurn)
                    || (pieceColor.equals(BLACK) && !whiteTurn)) {
                final Image i = getImage(currPiece.getPieceType(), currPiece.getPieceColor());
                g.drawImage(i, this.boardState.getCurrX(), this.boardState.getCurrY(), null);
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
    public void updateBoardState(BoardState newState) {
        this.boardState = newState;
        SquareState[][] squares = newState.getBoardSquareArray();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squareViews[x][y].setSquare(squares[x][y]);
                squareViews[x][y].repaint(); // force re-render
            }
        }

        this.repaint(); // force re-render of parent panel
    }

}
