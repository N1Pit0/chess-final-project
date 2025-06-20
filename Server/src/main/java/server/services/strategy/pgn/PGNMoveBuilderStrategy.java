package server.services.strategy.pgn;

public interface PGNMoveBuilderStrategy {
    String build(String from, String to) throws Exception;
}
