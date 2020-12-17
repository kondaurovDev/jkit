package jkit.entry;

import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReadyCommand {
    CommandDef commandDef;
    MethodContext methodContext;
}
