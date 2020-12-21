package jkit.entry;

import com.jkit.core.JKitEntry;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MethodContext implements JKitEntry.IMethodContext {
    JKitEntry.IPropMap params;
    JKitEntry.IPropMap user;
    JKitEntry.IUserLog userLog;
}
