package jkit.entry;

import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MethodContext {
    PropMap params;
    PropMap user;
    UserLog userLog;
}
