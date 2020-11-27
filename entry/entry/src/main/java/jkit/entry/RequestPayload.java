package jkit.entry;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.HashMap;
import io.vavr.control.Either;
import jkit.core.model.UserError;
import jkit.entry.IApi;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RequestPayload {

    String value;
    IApi.DataFormat dataFormat;

    public Either<UserError, HashMap<String, Object>> getMap() {
        return switch (dataFormat) {
            case json: yield json.deserializeToMap(value, Object.class);
            case yaml: yield yaml.deserializeToMap(value, Object.class);
        };
    }

    public Either<UserError, JsonNode> getJson() {
        return switch (dataFormat) {
            case json: yield json.parseRaw(value);
            case yaml: yield yaml.parseRaw(value);
        };
    }

    public <A> Either<UserError, A> deserialize(Class<A> clazz) {
        return switch (dataFormat) {
            case json: yield json.deserialize(value, clazz);
            case yaml: yield yaml.deserialize(value, clazz);
        };
    }

}
