package server.services.utils;

import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovementUtil {

    private static final int[][] LINEAR_DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    private static final int[][] DIAGONAL_DIRECTIONS = {
            {-1, -1}, {1, -1}, {1, 1}, {-1, 1}
    };

    public static List<SquareInterface> getLinearMoves(SquareInterface[][] squareArrayBoard, PieceInterface piece) {
        return getMovesInDirections(squareArrayBoard, piece, LINEAR_DIRECTIONS);
    }

    public static List<SquareInterface> getDiagonalMoves(SquareInterface[][] squareArrayBoard, PieceInterface piece) {
        return getMovesInDirections(squareArrayBoard, piece, DIAGONAL_DIRECTIONS);
    }

    private static List<SquareInterface> getMovesInDirections(SquareInterface[][] squareArrayBoard, PieceInterface piece, int[][] directions) {
        List<SquareInterface> legalSquares = new ArrayList<>();
        SquareInterface position = piece.getCurrentSquare();
        int x = position.getXNum();
        int y = position.getYNum();

        Arrays.stream(directions).forEach((direction) -> {
            int dy = direction[0];
            int dx = direction[1];
            int currentY = y + dy;
            int currentX = x + dx;

            while (isInBound(currentX, currentY)) {
                SquareInterface targetSquare = squareArrayBoard[currentY][currentX];

                if (targetSquare.isOccupied()) {
                    if (targetSquare.getOccupyingPiece().getPieceColor() != piece.getPieceColor()) {
                        legalSquares.add(targetSquare);
                    }
                    break;
                }

                legalSquares.add(targetSquare);
                currentY += dy;
                currentX += dx;
            }
        });

        return legalSquares;
    }

    public static boolean isInBound(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}
