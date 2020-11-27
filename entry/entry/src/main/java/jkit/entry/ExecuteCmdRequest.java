package jkit.entry;

import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class ExecuteCmdRequest<U> {
    RequestPayload payload;
    U user;
    IApi.ResponseType responseType;
    IApi.DataFormat responseFormat;
}
