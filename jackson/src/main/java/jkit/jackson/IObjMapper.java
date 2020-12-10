package jkit.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.Function1;
import io.vavr.control.Either;
import jkit.core.JKitData;
import jkit.core.JKitValidate;
import jkit.core.ext.StreamExt;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public interface IObjMapper
    extends JKitData.IObjMapper<JsonNode> {

    JKitValidate.IValidator getValidator();
    ObjectMapper getObjectMapper();

    default <C> Either<UserError, C> deserialize(Object input, Class<C> clazz) {
        if (input instanceof String)
            return deserializeFromString((String)input, clazz);

        if (input instanceof JsonNode)
            return deserializeFromNode((JsonNode) input, clazz);

        return convert(input, clazz);
    }

    default <C> Either<UserError, C> convert(Object obj, Class<C> clazz, String... fields) {
        return TryExt.get(
            () -> {
                if (clazz.isInstance(obj)) {
                    return clazz.cast(obj);
                }
                return getObjectMapper().convertValue(obj, clazz);
            },
            "Can't convert to class"
        )
        .flatMap(o -> getValidator().validate(o, fields));
    }

    default Either<UserError, String> serialize(Object obj) {
        return TryExt.get(
            () -> getSerializer().apply(obj),
            "Can't serialize object"
        );
    }

    default CheckedFunction0<InputStream> serializeToInputStream(Object obj) {
        return () -> StreamExt.fromString(getSerializer().apply(obj)).apply();
    }

    default CheckedFunction1<Object, String> getSerializer() {
        return (obj) -> getObjectMapper().writeValueAsString(obj);
    }

    default Either<UserError, JsonNode> toJsonNode(Object obj) {
        return TryExt.get(
            () -> getObjectMapper().valueToTree(obj),
            "Can't convert object to json node"
        );
    }

    default Either<UserError, Map<String, Object>> objToMap(Object node) {
        return TryExt.get(
            () -> getObjectMapper().convertValue(node, new TypeReference<Map<String, Object>>() {}),
            "object to Map"
        );
    }

    default Either<UserError, Map<String, String>> objToStringMap(Object node) {
        return objToMap(node)
            .map(map ->
                map
                    .entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString(), (prev, next) -> next, HashMap::new))
            );

    }

}
