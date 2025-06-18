package server.services.strategy;

import enums.PieceColor;
import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;
import server.services.strategy.common.PieceStrategy;
import server.services.utils.MovementUtil;

import java.util.ArrayList;
import java.util.List;

public class KingStrategy extends PieceStrategy {

    public KingStrategy(PieceInterface piece) {
        super(piece);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        List<SquareInterface> legalMoves = new ArrayList<>();

        int x = getPiece().getCurrentSquare().getXNum();
        int y = super.getPiece().getCurrentSquare().getYNum();
        PieceColor currentColor = super.getPiece().getPieceColor();

        // Check all adjacent squares in the 3x3 grid centered on (x, y)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Skip the current square (dx = 0, dy = 0)
                if (dx == 0 && dy == 0) {
                    continue;
                }

                // Compute the target square's coordinates
                int targetX = x + dx;
                int targetY = y + dy;

                // Check if the target square is valid and add to legal moves
                if (MovementUtil.isInBound(targetX, targetY)) {
                    SquareInterface targetSquare = squareArrayBoard[targetY][targetX];

                    if (!targetSquare.isOccupied()
                            || targetSquare.getOccupyingPiece().getPieceColor() != currentColor) {
                        legalMoves.add(targetSquare);
                    }
                }
            }
        }

        return legalMoves;
    }

}

