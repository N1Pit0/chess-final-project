package model.board;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import services.board.SquareInterface;
import services.enums.PieceColor;
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

}
