package server.services.strategy.common;

import lombok.Getter;
import server.services.board.SquareInterface;

import java.util.List;

@Getter
public abstract class PieceStrategy {
    private final PieceInterface piece;

    public PieceStrategy(PieceInterface piece) {
        this.piece = piece;
    }


    public abstract List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard);
}
