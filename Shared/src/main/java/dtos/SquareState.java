package dtos;

import dtos.enums.PieceColor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class SquareState implements Serializable {
    private final int x;
    private final int y;
    private final PieceColor squareColor;
//    private final PieceState occupyingPiece;

    public SquareState(int x, int y, PieceColor squareColor /*, PieceState occupyingPiece*/) {
        this.x = x;
        this.y = y;
        this.squareColor = squareColor;
//        this.occupyingPiece = occupyingPiece;
    }
}