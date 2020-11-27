package jkit.core.model.http;

import jkit.core.model.DataFormat;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RequestPayload {

    String value;
    DataFormat dataFormat;

}
