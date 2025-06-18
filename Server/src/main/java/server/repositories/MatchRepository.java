package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import server.model.entity.Match;

import java.util.List;

public interface MatchRepository extends MongoRepository<Match, String> {

    @Query(value = "{ $or: [ { 'user1.username': ?0 }, { 'user2.username': ?0 } ] }")
    List<Match> findByUser1_UsernameOrUser2_Username(String username);
}