package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import server.model.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    User findByEmail(String email);

}