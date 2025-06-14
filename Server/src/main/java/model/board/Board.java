package model.board;

import lombok.Getter;
import lombok.Setter;
import model.pieces.*;
import services.board.BoardInterface;
import services.board.SquareInterface;
import services.enums.PieceColor;
import services.strategy.common.PieceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static services.enums.ImagePath.*;
import static services.enums.PieceColor.BLACK;
import static services.enums.PieceColor.WHITE;

@Getter
@Setter
public class Board implements BoardInterface {
    private SquareInterface[][] boardSquareArray;

    // List of pieces and whether they are movable
    private List<PieceInterface> blackPieces;
    private List<PieceInterface> whitePieces;

    private King whiteKing;
    private King blackKing;

    public Board() {
        this.boardSquareArray = new SquareInterface[8][8];
        this.blackPieces = new ArrayList<>();
        this.whitePieces = new ArrayList<>();
    }

    @Override
    public Optional<PieceInterface> getWhiteKing() {
        return Optional.ofNullable(whiteKing);
    }

    @Override
    public void setWhiteKing(PieceInterface whiteKing) {
        this.whiteKing = (King) whiteKing;
    }

    @Override
    public Optional<PieceInterface> getBlackKing() {
        return Optional.ofNullable(blackKing);
    }

    @Override
    public void setBlackKing(PieceInterface blackKing) {
        this.blackKing = (King) blackKing;
    }

    @Override
    public void initializePieces() {

        for (int x = 0; x < 8; x++) {
            boardSquareArray[1][x].put(new Pawn(BLACK, boardSquareArray[1][x], RESOURCES_BPAWN_PNG.label));
            boardSquareArray[6][x].put(new Pawn(WHITE, boardSquareArray[6][x], RESOURCES_WPAWN_PNG.label));
        }

        boardSquareArray[7][3].put(new Queen(WHITE, boardSquareArray[7][3], RESOURCES_WQUEEN_PNG.label));
        boardSquareArray[0][3].put(new Queen(BLACK, boardSquareArray[0][3], RESOURCES_BQUEEN_PNG.label));

        this.blackKing = new King(BLACK, boardSquareArray[0][4], RESOURCES_BKING_PNG.label);
        this.whiteKing = new King(WHITE, boardSquareArray[7][4], RESOURCES_WKING_PNG.label);
        boardSquareArray[0][4].put(blackKing);
        boardSquareArray[7][4].put(whiteKing);

        boardSquareArray[0][0].put(new Rook(BLACK, boardSquareArray[0][0], RESOURCES_BROOK_PNG.label));
        boardSquareArray[0][7].put(new Rook(BLACK, boardSquareArray[0][7], RESOURCES_BROOK_PNG.label));
        boardSquareArray[7][0].put(new Rook(WHITE, boardSquareArray[7][0], RESOURCES_WROOK_PNG.label));
        boardSquareArray[7][7].put(new Rook(WHITE, boardSquareArray[7][7], RESOURCES_WROOK_PNG.label));

        boardSquareArray[0][1].put(new Knight(BLACK, boardSquareArray[0][1], RESOURCES_BKNIGHT_PNG.label));
        boardSquareArray[0][6].put(new Knight(BLACK, boardSquareArray[0][6], RESOURCES_BKNIGHT_PNG.label));
        boardSquareArray[7][1].put(new Knight(WHITE, boardSquareArray[7][1], RESOURCES_WKNIGHT_PNG.label));
        boardSquareArray[7][6].put(new Knight(WHITE, boardSquareArray[7][6], RESOURCES_WKNIGHT_PNG.label));

        boardSquareArray[0][2].put(new Bishop(BLACK, boardSquareArray[0][2], RESOURCES_BBISHOP_PNG.label));
        boardSquareArray[0][5].put(new Bishop(BLACK, boardSquareArray[0][5], RESOURCES_BBISHOP_PNG.label));
        boardSquareArray[7][2].put(new Bishop(WHITE, boardSquareArray[7][2], RESOURCES_WBISHOP_PNG.label));
        boardSquareArray[7][5].put(new Bishop(WHITE, boardSquareArray[7][5], RESOURCES_WBISHOP_PNG.label));


        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                blackPieces.add(boardSquareArray[y][x].getOccupyingPiece());
                whitePieces.add(boardSquareArray[7 - y][x].getOccupyingPiece());
            }
        }
    }

    @Override
    public void initializeBoardSquares() {

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                PieceColor squareColor = ((x + y) % 2 == 0) ? WHITE : BLACK;
                boardSquareArray[y][x] = new Square(squareColor, x, y);
            }
        }
    }
}
