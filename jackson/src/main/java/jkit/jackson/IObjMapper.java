package jkit.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import jkit.core.JKitData;
import jkit.core.JKitValidate;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;

import java.util.Map;

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
            () -> getObjectMapper().writeValueAsString(obj),
            "Can't serialize object"
        );
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

}
