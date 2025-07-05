package server.services.strategy.pgn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.services.board.BoardService;
import server.services.board.SquareInterface;
import server.services.strategy.common.PieceInterface;

import java.util.Arrays;

/***
 * expects that the move is valid
 */
@Service
public class CustomPGNMoveBuilderStrategy implements PGNMoveBuilderStrategy {

    private final BoardService boardService;

    @Autowired
    public CustomPGNMoveBuilderStrategy(BoardService boardService) {
        this.boardService = boardService;
    }

    @Override
    public String build(String from, String to) throws Exception {
        PieceInterface piece = identifyPieceFromString(from);

        SquareInterface targetSquare = Arrays.stream(boardService.getBoardSquareArray())
                .flatMap(Arrays::stream)
                .filter(x -> x.toAlgebraic().equals(to))
                .findFirst()
                .orElseThrow(() -> new Exception("Target square not found"));

        String piecePGNSymbol = piece.toPGNFormatSymbol();
        String currentFrom = piece.getCurrentSquare().toAlgebraic();

        // ♟️ Handle castling
        int abs = Math.abs(from.charAt(0) - to.charAt(0));
        if (piecePGNSymbol.equals("K") && abs == 2) {
            return (to.charAt(0) == 'g') ? "O-O" : "O-O-O";
        }

        StringBuilder result = new StringBuilder();
        result.append(piecePGNSymbol);

        var sameColorPieces = boardService.isWhiteTurn()
                ? boardService.getWhitePieces()
                : boardService.getBlackPieces();

        PieceInterface possibleOtherPiece = sameColorPieces.stream()
                .filter(x -> x != piece && x.toPGNFormatSymbol().equals(piecePGNSymbol))
                .findFirst()
                .orElse(null);

        if (possibleOtherPiece != null && possibleOtherPiece.getLegalMoves(boardService.getBoardSquareArray())
                .stream()
                .map(SquareInterface::toAlgebraic)
                .toList()
                .contains(targetSquare.toAlgebraic())) {
            String otherFrom = possibleOtherPiece.getCurrentSquare().toAlgebraic();
            if (currentFrom.charAt(0) == otherFrom.charAt(0)) {
                result.append(currentFrom.charAt(1)); // disambiguate by rank
            } else {
                result.append(currentFrom.charAt(0)); // disambiguate by file
            }
        }

        // ♟️ Handle en passant (treated like a capture)
        boolean isEnPassant = piecePGNSymbol.isEmpty() &&
                !targetSquare.isOccupied() &&
                abs == 1;

        boolean isCapture = targetSquare.isOccupied() || isEnPassant;

        if (isCapture) {
            // For pawn captures: prepend file
            if (piecePGNSymbol.isEmpty()) {
                result.append(from.charAt(0));
            }
            result.append("x");
        }

        result.append(targetSquare.toAlgebraic());

        return result.toString();
    }

    private PieceInterface identifyPieceFromString(String from) throws Exception {
        SquareInterface square = Arrays.stream(boardService.getBoardSquareArray()).flatMap(Arrays::stream).filter(x -> x.toAlgebraic().equals(from)).findFirst().orElse(null);
        if (square == null) {
            throw new Exception("Expected That the square will be occupied by Piece");
        }
        return square.getOccupyingPiece();
    }


}
