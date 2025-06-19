package shared.dtos;

import shared.enums.PieceColor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

@ToString
@Getter
@Setter
public class SquareState implements Serializable {
    private  int x;
    private  int y;
    private  PieceColor squareColor;
    private  PieceState occupyingPiece;

    public SquareState(int x, int y, PieceColor squareColor, PieceState occupyingPiece) {
        this.x = x;
        this.y = y;
        this.squareColor = squareColor;
        this.occupyingPiece = occupyingPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SquareState that)) return false;
        return x == that.x && y == that.y && squareColor == that.squareColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, squareColor);
    }
}