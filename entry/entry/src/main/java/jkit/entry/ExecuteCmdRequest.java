package jkit.entry;

import jkit.core.iface.Entry;
import jkit.core.model.http.RequestPayload;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class ExecuteCmdRequest<U> implements Entry.IExecuteCmdRequest<U> {
    RequestPayload payload;
    U user;
    Entry.ResponseType responseType;
    Entry.DataFormat responseFormat;
}
