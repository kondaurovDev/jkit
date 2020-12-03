package jkit.entry;

import jkit.core.iface.Entry;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MethodContext implements Entry.IMethodContext {
    Entry.IPropMap params;
    Entry.IPropMap user;
    Entry.IUserLog userLog;
}
