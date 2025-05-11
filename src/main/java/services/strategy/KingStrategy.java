package services.strategy;

import services.board.BoardService;
import services.board.SquareInterface;
import services.enums.PieceColor;
import services.strategy.common.PieceInterface;
import services.strategy.common.PieceStrategy;
import services.utils.MovementUtil;

import java.util.ArrayList;
import java.util.List;

public class KingStrategy extends PieceStrategy {

    public KingStrategy(PieceInterface piece) {
        super(piece);
    }

    @Override
    public List<SquareInterface> getLegalMoves(BoardService boardService) {
        List<SquareInterface> legalMoves = new ArrayList<>();
        SquareInterface[][] squareBoard = boardService.getBoard();

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
                    SquareInterface targetSquare = squareBoard[targetY][targetX];

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

