package server.services.pgn;

import server.model.entity.Match;

public interface PgnService {

    boolean save(Match match);

    boolean addMove(String from, String to) throws Exception;
}
