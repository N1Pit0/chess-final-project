package server.services.strategy;

import server.model.pieces.common.Piece;
import shared.enums.PieceColor;
import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;
import server.services.strategy.common.PieceStrategy;

import java.util.ArrayList;
import java.util.List;

import static server.services.utils.MovementUtil.isInBound;

public class PawnStrategy extends PieceStrategy {

    public PawnStrategy(PieceInterface piece) {
        super(piece);
    }

    @Override
    public List<SquareInterface> getLegalMoves(SquareInterface[][] squareArrayBoard) {
        List<SquareInterface> legalMoves = new ArrayList<>();

        int x = super.getPiece().getCurrentSquare().getXNum();
        int y = super.getPiece().getCurrentSquare().getYNum();
        PieceColor currentPieceColor = super.getPiece().getPieceColor();

        // Vertical direction: positive for BLACK, negative for WHITE
        int direction = (currentPieceColor.equals(PieceColor.BLACK)) ? 1 : -1;

        PieceColor oppositePieceColor = currentPieceColor.equals(PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

        // Add potential moves using helper methods
        addStraightMove(legalMoves, squareArrayBoard, x, y, direction);
        addAttackMoves(legalMoves, squareArrayBoard, x, y, direction,
                currentPieceColor, oppositePieceColor);

        return legalMoves;
    }

    // Helper method for straight moves (forward moves)
    private void addStraightMove(List<SquareInterface> legalMoves, SquareInterface[][] board, int x, int y, int direction) {
        // First move (can move two steps if not yet moved)
        PieceInterface pawn = getPiece();
        if (!(pawn.isWasMoved()) && isInBound(y + 2 * direction, x) && !board[y + 2 * direction][x].isOccupied()) {
            legalMoves.add(board[y + 2 * direction][x]);
        }
        // Regular move (one step forward)
        if (isInBound(y + direction, x) && !board[y + direction][x].isOccupied()) {
            legalMoves.add(board[y + direction][x]);
        }
    }

    // Helper method for diagonal attack moves
    private void addAttackMoves(List<SquareInterface> legalMoves, SquareInterface[][] board,
                                int x, int y, int direction,
                                PieceColor friendlyPieceColor, PieceColor oppositePieceColor) {
        // Check diagonal right
        if (isInBound(y + direction, x + 1)
                && board[y + direction][x + 1].isOccupied()
                && !board[y + direction][x + 1].getOccupyingPiece().getPieceColor().equals(friendlyPieceColor)
        ) {
            legalMoves.add(board[y + direction][x + 1]);
        }
        // Check diagonal left
        if (isInBound(y + direction, x - 1)
                && board[y + direction][x - 1].isOccupied()
                && !board[y + direction][x - 1].getOccupyingPiece().getPieceColor().equals(friendlyPieceColor)
        ) {
            legalMoves.add(board[y + direction][x - 1]);
        }
    }

}
