package jkit.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.Function1;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import jkit.core.ext.TryExt;
import lombok.val;

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

    default Try<JsonNode> merge(JsonNode node1, JsonNode node2) {
        return TryExt.get(
            () -> getObjectMapper()
                .readerForUpdating(node1)
                .readValue(node2),
            "Can't merge json"
        );
    }
}
