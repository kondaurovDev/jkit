package jkit.entry;

import jkit.core.JKitEntry;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class ExecuteCmdRequest implements JKitEntry.IExecuteCmdRequest {
    String commandName;
    PropMap payload;
    PropMap user;
}
