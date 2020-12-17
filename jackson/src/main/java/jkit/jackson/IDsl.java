package jkit.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Option;
import io.vavr.control.Try;

public interface IDsl {

    default <A> Try<A> getPath(
        JsonNode node,
        String path,
        IFactory.IJsonTypeTransformer<A> transformer
    ) {
        return getPathOpt(node, path)
            .toTry(() -> new Error("Json path doesn't exists: " + path))
            .flatMap(transformer::apply);
    }

    default Option<JsonNode> getPathOpt(
        JsonNode node,
        String path
    ) {
        return Option
            .of(node.get(path));
    }

    default Try<JsonNode> getPath(JsonNode node, String path) {
        return getPath(node, path, Try::success);
    }

}
