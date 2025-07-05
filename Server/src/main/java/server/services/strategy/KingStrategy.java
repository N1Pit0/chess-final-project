package server.services.strategy;

import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;
import server.services.strategy.common.PieceStrategy;
import server.services.utils.MovementUtil;
import shared.enums.PieceColor;

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

        int xStart, xEnd, yStart = -1, yEnd = 1;

        if (!getPiece().isWasMoved()) {
            xStart = -2;
            xEnd = 2;
        } else {
            xStart = -1;
            xEnd = 1;
        }

        // Check all adjacent squares in the 3x3 grid centered on (x, y)
        for (int dx = xStart; dx <= xEnd; dx++) {
            for (int dy = yStart; dy <= yEnd; dy++) {
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

