package dtos;

import enums.GameStatusType;
import enums.PieceColor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @param statusType     CHECK, CHECKMATE, STALEMATE, etc.
 * @param affectedPlayer who's in check / loses / etc.
 */

public record GameStatus(GameStatusType statusType, PieceColor affectedPlayer) implements Serializable {
}

