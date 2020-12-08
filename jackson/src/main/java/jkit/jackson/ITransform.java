package jkit.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.collection.Stream;
import io.vavr.control.Either;
import jkit.core.ext.ListExt;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import lombok.val;

import java.util.Map;

public interface ITransform extends IDeserialize {

    default ObjectNode transformKeys(JsonNode node, Function1<String, String> handle) {

        val result = getObjectMapper().createObjectNode();

        Stream
            .ofAll(node::fields)
            .forEach(f -> {
                val key = handle.apply(f.getKey());
                result.set(key, f.getValue());
            });

        return result;

    }

    default Either<UserError, Map<String, String>> transformMap(
        Map<String, ?> input
    ) {

        return ListExt.applyToEach(
            input.entrySet(),
            v -> {
                val t = v.getValue().getClass();
                if (t.isInstance(Map.class)) {
                    return Either.right((String)v.getValue());
                }

            },
            "transform map",
            true
        ).map(lst -> lst.toJavaMap(t -> t));

    }

    default Either<UserError, JsonNode> merge(JsonNode node1, JsonNode node2) {
        return TryExt.get(
            () -> getObjectMapper()
                .readerForUpdating(node1)
                .readValue(node2),
            "Can't merge json"
        );
    }
}
