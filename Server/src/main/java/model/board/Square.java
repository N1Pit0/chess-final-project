package model.board;

import dtos.SquareState;
import dtos.enums.PieceColor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import services.board.SquareInterface;
import services.strategy.common.PieceInterface;


@Getter
@Setter
@EqualsAndHashCode(exclude = {"squareColor", "occupyingPiece"}, callSuper = false)
public class Square implements SquareInterface {
    private final PieceColor squareColor;
    private PieceInterface occupyingPiece;

    private int xNum;
    private int yNum;

    public Square(PieceColor squareColor, int xNum, int yNum) {

        this.squareColor = squareColor;
        this.xNum = xNum;
        this.yNum = yNum;

    }

    public boolean isOccupied() {
        return (this.occupyingPiece != null);
    }

    public void put(PieceInterface piece) {
        this.occupyingPiece = piece;
        piece.setCurrentSquare(this);
    }

    @Override
    public SquareState toSquareState() {
        return new SquareState(xNum,yNum,squareColor,null);
    }

    @Override
    public String toAlgebraic() {
        char file = (char) ('a' + xNum);       // xNum: 0 → 'a', 1 → 'b', ..., 7 → 'h'
        int rank = 8 - yNum;                   // yNum: 0 → 8, 1 → 7, ..., 7 → 1
        return String.valueOf(file) + rank;
    }


}
