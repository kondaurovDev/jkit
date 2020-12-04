package jkit.entry;

import jkit.core.JKitEntry;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReadyCommand {
    JKitEntry.ICommandDef commandDef;
    JKitEntry.IMethodContext methodContext;
}
