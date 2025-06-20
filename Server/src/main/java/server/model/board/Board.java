package server.model.board;

import org.springframework.stereotype.Component;
import shared.dtos.BoardState;
import shared.dtos.PieceState;
import shared.dtos.SquareState;
import shared.enums.PieceColor;
import lombok.Getter;
import lombok.Setter;
import server.model.pieces.*;
import server.services.board.BoardInterface;
import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component
@Getter
@Setter
public class Board implements BoardInterface {
    private SquareInterface[][] boardSquareArray;

    // List of pieces and whether they are movable
    private List<PieceInterface> blackPieces;
    private List<PieceInterface> whitePieces;

    private King whiteKing;
    private King blackKing;

    private boolean isWhiteTurn;

    public Board() {

        this.boardSquareArray = new SquareInterface[8][8];
        this.blackPieces = new ArrayList<>();
        this.whitePieces = new ArrayList<>();
        initializeBoardSquares();
        initializePieces();
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
            boardSquareArray[1][x].put(new Pawn(PieceColor.BLACK, boardSquareArray[1][x]));
            boardSquareArray[6][x].put(new Pawn(PieceColor.WHITE, boardSquareArray[6][x]));
        }

        boardSquareArray[7][3].put(new Queen(PieceColor.WHITE, boardSquareArray[7][3]));
        boardSquareArray[0][3].put(new Queen(PieceColor.BLACK, boardSquareArray[0][3]));

        this.blackKing = new King(PieceColor.BLACK, boardSquareArray[0][4]);
        this.whiteKing = new King(PieceColor.WHITE, boardSquareArray[7][4]);
        boardSquareArray[0][4].put(blackKing);
        boardSquareArray[7][4].put(whiteKing);

        boardSquareArray[0][0].put(new Rook(PieceColor.BLACK, boardSquareArray[0][0]));
        boardSquareArray[0][7].put(new Rook(PieceColor.BLACK, boardSquareArray[0][7]));
        boardSquareArray[7][0].put(new Rook(PieceColor.WHITE, boardSquareArray[7][0]));
        boardSquareArray[7][7].put(new Rook(PieceColor.WHITE, boardSquareArray[7][7]));

        boardSquareArray[0][1].put(new Knight(PieceColor.BLACK, boardSquareArray[0][1]));
        boardSquareArray[0][6].put(new Knight(PieceColor.BLACK, boardSquareArray[0][6]));
        boardSquareArray[7][1].put(new Knight(PieceColor.WHITE, boardSquareArray[7][1]));
        boardSquareArray[7][6].put(new Knight(PieceColor.WHITE, boardSquareArray[7][6]));

        boardSquareArray[0][2].put(new Bishop(PieceColor.BLACK, boardSquareArray[0][2]));
        boardSquareArray[0][5].put(new Bishop(PieceColor.BLACK, boardSquareArray[0][5]));
        boardSquareArray[7][2].put(new Bishop(PieceColor.WHITE, boardSquareArray[7][2]));
        boardSquareArray[7][5].put(new Bishop(PieceColor.WHITE, boardSquareArray[7][5]));


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
                PieceColor squareColor = ((x + y) % 2 == 0) ? PieceColor.WHITE : PieceColor.BLACK;
                boardSquareArray[y][x] = new Square(squareColor, x, y);
            }
        }
    }

    @Override
    public BoardState toBoardState() {
        BoardState boardState = new BoardState();
        SquareState[][] squareStateArray = new SquareState[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (boardSquareArray[x][y] != null)
                    squareStateArray[x][y] = boardSquareArray[x][y].toSquareState();
            }
        }
        boardState.setBoardSquareArray(squareStateArray);
        List<PieceState> whitePieceStates = whitePieces.stream().map(PieceInterface::toPieceState).toList();
        List<PieceState> blackPieceStates = blackPieces.stream().map(PieceInterface::toPieceState).toList();
        PieceState whiteKingState = whiteKing != null ? whiteKing.toPieceState() : null;
        PieceState blackKingState = blackKing != null ? blackKing.toPieceState() : null;

        return new BoardState(squareStateArray,blackPieceStates, whitePieceStates, whiteKingState, blackKingState, isWhiteTurn);


    }

}
