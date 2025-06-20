package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.model.entity.Match;
import server.repositories.MatchRepository;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> findMatchesByUsername(String username) {
        return matchRepository.findByUser1_UsernameOrUser2_Username(username);
    }

    public List<Match> findAllMatches() {
        return matchRepository.findAll();
    }

    public boolean saveMatch(Match match) {
        try {
            matchRepository.save(match);
            return true;
        }catch (Exception e){
            System.err.println(e.getMessage());
            return false;
        }
    }

}