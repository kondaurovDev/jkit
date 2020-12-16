package jkit.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jkit.core.model.UserError;

public interface IDsl {

    default <A> Either<UserError, A> getPath(
        JsonNode node,
        String path,
        IFactory.IJsonTypeTransformer<A> transformer
    ) {
        return getPathOpt(node, path)
            .toEither(() -> UserError.create("Json path doesn't exists: " + path))
            .flatMap(transformer::apply);
    }

    default Option<JsonNode> getPathOpt(
        JsonNode node,
        String path
    ) {
        return Option
            .of(node.get(path));
    }

    default Either<UserError, JsonNode> getPath(JsonNode node, String path) {
        return getPath(node, path, v -> Either.right(v));
    }

}
