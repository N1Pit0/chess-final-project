package server.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "matches")
public class Match {

    @Id
    private String id;

    @DBRef
    private User user1;

    @DBRef
    private User user2;

    private String result;

    private String pgn;

    private LocalDateTime datePlayed;

}