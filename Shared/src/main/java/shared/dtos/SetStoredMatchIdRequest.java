package shared.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SetStoredMatchIdRequest implements Serializable {
    private final String matchId;

    public SetStoredMatchIdRequest(String matchId) {
        this.matchId = matchId;
    }
}
