package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import server.model.entity.Match;
import server.model.entity.User;
import server.repositories.MatchRepository;
import server.repositories.UserRepository;
import server.services.UserService;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {UserRepository.class, MatchRepository.class })
public class Application implements CommandLineRunner {
    private UserService userService;
    private MatchRepository matchRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMatchRepository(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        userService.save(new User("pika", "email", "password"));

//        userService.save(new User("nika", "email", "password"));

        User user1 = userService.findByUsername("pika");

        User user2 = userService.findByUsername("nika");

//        Match match = new Match();
//
//        match.setUser1(user1);
//        match.setUser2(user2);
//        match.setResult("1-0");
//
//        matchRepository.save(match);

        matchRepository.findAll().forEach(System.out::println);

    }
}