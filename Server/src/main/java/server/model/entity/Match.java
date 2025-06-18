package server.model.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "match_id")
    private Long id;

    @ManyToMany(mappedBy = "matches")
    private Set<Match> matches = new HashSet<>();

}
