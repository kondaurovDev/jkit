package jkit.entry;

import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommandFlag {
    Boolean saveEvent;
    Boolean parallelRun;

    public static final CommandFlag simpleTask = CommandFlag.of(
        false,
        true
    );

    public static final CommandFlag job = CommandFlag.of(
        true,
        false
    );
}