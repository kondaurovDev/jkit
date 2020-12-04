package jkit.entry;

import jkit.core.iface.Entry;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class ExecuteCmdRequest implements Entry.IExecuteCmdRequest {
    String commandName;
    PropMap payload;
    PropMap user;
}
