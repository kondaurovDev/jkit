package jkit.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.CheckedFunction1;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import jkit.core.JKitData;
import jkit.core.JKitValidate;
import jkit.core.ext.IOExt;
import jkit.core.ext.StreamExt;
import jkit.core.ext.TryExt;
import lombok.val;

import java.util.Map;

public interface IObjMapper
    extends JKitData.IObjMapper<JsonNode> {

    JKitValidate.IValidator getValidator();
    ObjectMapper getObjectMapper();

    default <C> Try<C> deserialize(Object input, Class<C> clazz) {
        if (input instanceof String)
            return deserializeFromString((String)input, clazz);

        if (input instanceof JsonNode)
            return deserializeFromNode((JsonNode) input, clazz);

        return convert(input, clazz);
    }

    default <C> Try<C> convert(Object obj, Class<C> clazz, String... fields) {
        return Try
            .of(() -> {
                if (clazz.isInstance(obj)) {
                    return clazz.cast(obj);
                }
                return getObjectMapper().convertValue(obj, clazz);
            })
            .onFailure(e -> IOExt.debug("Can't convert to class", e))
            .flatMap(o -> getValidator().validate(o, fields));
    }

    default Try<String> serialize(Object obj) {
        return Try
            .of(() -> getSerializer().apply(obj))
            .onFailure(e -> IOExt.debug("Can't serialize object",  e));
    }

    default CheckedFunction1<Object, String> getSerializer() {
        return (obj) -> getObjectMapper().writeValueAsString(obj);
    }

    default Try<JsonNode> toJsonNode(Object obj) {
        return TryExt.get(
            () -> getObjectMapper().valueToTree(obj),
            "Can't convert object to json node"
        );
    }

    default Try<Map<String, Object>> objToMap(Object node) {
        return Try
            .of(() -> getObjectMapper().convertValue(node, new TypeReference<Map<String, Object>>() {}))
            .onFailure(e -> IOExt.debug("object to Map", e));
    }

    default Try<Map<String, String>> objToStringMap(Object node) {
        return objToMap(node)
            .map(map ->
                StreamExt.streamToMap(
                    map.entrySet().stream(),
                    Map.Entry::getKey,
                    v -> v.getValue().toString()
                )
            );
    }

    default ObjectNode createEmptyObject() {
        return getObjectMapper().createObjectNode();
    }

    default ObjectNode createObjectNode(Iterable<Tuple2<String, Object>> map) {
        val result = createEmptyObject();
        map.forEach(t -> result.putPOJO(t._1, t._2));
        return result;
    }

}
