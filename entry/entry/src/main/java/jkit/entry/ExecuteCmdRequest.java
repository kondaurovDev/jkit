package jkit.entry;

import jkit.core.iface.Entry;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class ExecuteCmdRequest implements Entry.IExecuteCmdRequest {
    PropMap payload;
    PropMap user;
    Entry.ResponseType responseType;
}
