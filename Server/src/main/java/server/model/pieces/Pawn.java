package server.model.pieces;

import lombok.Getter;
import server.model.pieces.common.Piece;
import server.services.board.Move;
import server.services.board.SquareInterface;
import shared.enums.PieceColor;
import shared.enums.PieceType;

import java.util.List;

@Getter
public class Pawn extends Piece {

    public Pawn(PieceColor pieceColor, SquareInterface initSq, Move move) {
        super(pieceColor, initSq, move);
        setPieceType(PieceType.PAWN);
    }

    @Override
    public String toPGNFormatSymbol() {
        return "";
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        return getPieceStrategy().getLegalMoves(squareArrayBoard);
    }

}
