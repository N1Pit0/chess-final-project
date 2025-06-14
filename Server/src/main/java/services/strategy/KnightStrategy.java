package services.strategy;

import services.board.SquareInterface;
import services.strategy.common.PieceInterface;
import services.strategy.common.PieceStrategy;
import services.utils.MovementUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KnightStrategy extends PieceStrategy {

    public KnightStrategy(PieceInterface piece) {
        super(piece);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        List<SquareInterface> legalMoves = new ArrayList<>();
        SquareInterface currentSquare = getPiece().getCurrentSquare();

        int x = currentSquare.getXNum();
        int y = currentSquare.getYNum();

        int[][] directions = {
                {2, 1}, {1, 2}, {-1, 2},
                {-2, 1}, {-2, -1}, {-1, -2},
                {1, -2}, {2, -1}
        };

        Arrays.stream(directions).forEach(direction -> {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if (MovementUtil.isInBound(newX, newY)) {
                SquareInterface targetSquare = squareArrayBoard[newY][newX];

                if (!targetSquare.isOccupied()
                        || targetSquare.getOccupyingPiece().getPieceColor() != getPiece().getPieceColor()) {
                    legalMoves.add(targetSquare);
                }
            }

        });

        return legalMoves;
    }
}
