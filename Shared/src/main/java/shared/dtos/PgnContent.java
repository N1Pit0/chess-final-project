package shared.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PgnContent implements Serializable {
    private String user1;
    private String user2;
    private String result;
    private String gameContent;
}
