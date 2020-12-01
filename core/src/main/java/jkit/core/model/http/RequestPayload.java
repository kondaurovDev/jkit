package jkit.core.model.http;

import io.vavr.collection.HashMap;
import io.vavr.control.Either;
import jkit.core.iface.Entry;
import jkit.core.model.UserError;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RequestPayload implements Entry.IRequestPayload {

    String value;
    Entry.DataFormat dataFormat;

    @Override
    public Either<UserError, HashMap<String, Object>> getMap() {
        return null;
    }
}
