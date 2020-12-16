package jkit.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.Tuple2;
import jkit.core.JKitValidate;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ObjectMapperExt
    implements IDeserialize, ITransform {

    ObjectMapper objectMapper;
    JKitValidate.IValidator validator;

    public ObjectNode newObj() {
        return objectMapper
            .createObjectNode();
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
