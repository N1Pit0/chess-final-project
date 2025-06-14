package controller;

import services.board.SquareInterface;

public interface GameController {
    boolean handlePress(SquareInterface square);

    boolean handleRelease(SquareInterface square);
}
