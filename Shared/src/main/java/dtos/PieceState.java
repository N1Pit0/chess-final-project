package dtos;

import dtos.enums.PieceColor;
import dtos.enums.PieceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@ToString
@Getter
@Setter
public class PieceState implements Serializable {
    private PieceColor pieceColor;
    private SquareState currentSquare;
    private PieceType pieceType;
    private boolean wasMoved;

    public PieceState(PieceColor pieceColor, SquareState currentSquare, PieceType pieceType, boolean wasMoved) {
        this.pieceColor = pieceColor;
        this.currentSquare = currentSquare;
        this.pieceType = pieceType;
        this.wasMoved = wasMoved;
    }
}
