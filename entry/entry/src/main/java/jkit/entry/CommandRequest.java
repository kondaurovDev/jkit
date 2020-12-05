package jkit.entry;

import jkit.core.JKitEntry;
import lombok.*;

import java.util.Map;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class CommandRequest implements JKitEntry.ICommandRequest {
    String commandName;
    Map<String, Object> payload;
    Map<String, Object> user;
}
