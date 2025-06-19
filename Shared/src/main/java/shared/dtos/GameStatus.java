package shared.dtos;

import shared.enums.GameStatusType;
import shared.enums.PieceColor;

import java.io.Serializable;

/**
 * @param statusType     CHECK, CHECKMATE, STALEMATE, etc.
 * @param affectedPlayer who's in check / loses / etc.
 */

public record GameStatus(GameStatusType statusType, PieceColor affectedPlayer) implements Serializable {
}

