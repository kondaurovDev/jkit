package jkit.entry;

import lombok.*;
import java.util.Map;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class CommandRequest {
    String commandName;
    Map<String, Object> payload;
    Map<String, Object> user;
}
