package controller;

import services.board.BoardInterface;
import services.board.BoardService;
import services.board.BoardServiceImpl;

public class ClientGameController {
    private BoardService boardService;

    public ClientGameController(BoardInterface boardInterface) {
        this.boardService = new BoardServiceImpl(boardInterface);
    }







}
