package services.strategy;

import services.board.SquareInterface;
import services.strategy.common.PieceInterface;
import services.strategy.common.PieceStrategy;
import services.utils.MovementUtil;

import java.util.ArrayList;
import java.util.List;

public class QueenStrategy extends PieceStrategy {

    public QueenStrategy(PieceInterface piece) {
        super(piece);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {

        List<SquareInterface> legalMoves = new ArrayList<>(MovementUtil.getLinearMoves(squareArrayBoard, this.getPiece()));

        legalMoves.addAll(MovementUtil.getDiagonalMoves(squareArrayBoard, super.getPiece()));

        return legalMoves;
    }

}
