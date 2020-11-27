package jkit.entry;

import jkit.core.model.http.RequestPayload;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class ExecuteCmdRequest<U> {
    RequestPayload payload;
    U user;
    IApi.ResponseType responseType;
    IApi.DataFormat responseFormat;
}
