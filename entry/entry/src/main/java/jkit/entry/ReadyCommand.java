package jkit.entry;

import jkit.core.iface.Entry;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReadyCommand {
    Entry.ICommandDef commandDef;
    Entry.IMethodContext methodContext;
}
