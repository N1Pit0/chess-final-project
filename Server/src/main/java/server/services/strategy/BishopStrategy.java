package server.services.strategy;

import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;
import server.services.strategy.common.PieceStrategy;
import server.services.utils.MovementUtil;

import java.util.List;

public class BishopStrategy extends PieceStrategy {


    public BishopStrategy(PieceInterface piece) {
        super(piece);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {

        return MovementUtil.getDiagonalMoves(squareArrayBoard, super.getPiece());
    }
}
