package jkit.entry;

import lombok.*;

import java.sql.Timestamp;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommandEvent {
    Timestamp endTime;
    Timestamp startTime;
    String eventName;
    String logs;
    String error;
}
