package jkit.entry;

import jkit.core.JKitEntry;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class CommandRequest implements JKitEntry.ICommandRequest {
    String commandName;
    PropMap payload;
    PropMap user;
}
