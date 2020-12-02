package jkit.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.Tuple2;
import io.vavr.collection.*;
import io.vavr.control.Either;
import jkit.core.ext.*;
import jkit.core.iface.IObjMapper;
import jkit.core.iface.IValidator;
import jkit.core.model.UserError;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ObjectMapperExt
    implements IObjMapper, IDeserialize, ITransform {

    ObjectMapper objectMapper;
    IValidator validator;

    public JacksonModule.IJsonFactory getFactory(Object o) {
        return () -> toJsonNode(o);
    }

    public ObjectNode newObj() {
        return objectMapper
            .createObjectNode();
    }

    public <C> Either<UserError, C> deserialize(Object input, Class<C> clazz) {
        if (input instanceof String)
            return deserializeFromString((String)input, clazz);

        if (input instanceof JsonNode)
            return deserializeFromNode((JsonNode) input, clazz);

        return convert(input, clazz);
    }

    public <C> Either<UserError, C> convert(Object obj, Class<C> clazz, String... fields) {
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

    public Either<UserError, String> serialize(Object obj) {
        return TryExt.get(
            () -> objectMapper.writeValueAsString(obj),
            "Can't serialize object"
        );
    }

    public Either<UserError, JsonNode> toJsonNode(Object obj) {
        return TryExt.get(
            () -> objectMapper.valueToTree(obj),
            "Can't convert object to json node"
        );
    }

    public Either<UserError, Map<String, Object>> objToMap(Object node) {
        return TryExt.get(
            () -> objectMapper.convertValue(node, new TypeReference<Map<String, Object>>() {}),
            "object to Map"
        );
    }

    public ObjectNode createEmptyObject() {
        return objectMapper.createObjectNode();
    }

    public ObjectNode createObjectNode(Iterable<Tuple2<String, Object>> map) {
        val result = createEmptyObject();
        map.forEach(t -> result.putPOJO(t._1, t._2));
        return result;
    }

}
