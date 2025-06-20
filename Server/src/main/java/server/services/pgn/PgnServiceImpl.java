package server.services.pgn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.model.entity.Match;
import server.services.MatchService;
import server.services.board.BoardService;
import server.services.strategy.pgn.PGNMoveBuilderStrategy;

import java.time.LocalDateTime;

@Service
public class PgnServiceImpl implements PgnService {
    private final MatchService matchService;
    private final BoardService boardService;
    private final PGNMoveBuilderStrategy pgnMoveBuilderStrategy;

    private final StringBuilder result = new StringBuilder();
    private int moveCounter = 1;

    @Autowired
    public PgnServiceImpl(MatchService matchService, BoardService boardService, PGNMoveBuilderStrategy pgnMoveBuilderStrategy) {
        this.matchService = matchService;
        this.boardService = boardService;
        this.pgnMoveBuilderStrategy = pgnMoveBuilderStrategy;
    }

    @Override
    public boolean save(Match match) {
        match.setPgn(result.toString());
        match.setDatePlayed(LocalDateTime.now());
        return matchService.saveMatch(match);
    }

    @Override
    public boolean addMove(String from, String to) {
        String pgnFormatMove = null;
        try {
            pgnFormatMove = pgnMoveBuilderStrategy.build(from, to);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        if (boardService.isWhiteTurn()) {
            result.append(moveCounter).append(".").append(pgnFormatMove).append(" ");
        } else {
            result.append(pgnFormatMove).append(" ");
            moveCounter++;
        }
        return true;
    }
}
