package dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Move {

    private String from;
    private String to;
    public Move(String from, String to) {
        this.from = from;
        this.to = to;
    }
    public Move() {}
}
