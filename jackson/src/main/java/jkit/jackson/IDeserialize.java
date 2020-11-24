package jkit.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jkit.core.ext.*;
import jkit.core.model.UserError;
import lombok.val;
import jkit.core.iface.Validator;

public interface IDeserialize {

    ObjectMapper getObjectMapper();
    Validator getValidator();

    static Option<String> nonEmptyJsonString(String raw) {
        if (raw.isEmpty()) {
            return Option.none();
        } else {
            return Option.some(raw);
        }
    }

    default Either<UserError, JsonNode> parseRaw(String raw) {

        return nonEmptyJsonString(raw)
            .fold(
                () -> Either.<UserError, JsonNode>right(NullNode.getInstance()),
                v -> TryExt
                .get(
                    () -> getObjectMapper().readTree(v),
                    "read tree"
                )
            )
            .mapLeft(e -> e.withError("Can't parse string to json: " + raw));

    }

    default Either<UserError, JsonNode> parseBase64(String encoded) {
        return StringExt
            .decodeBase64(encoded)
            .toEither(UserError.create("Not valid base64"))
            .flatMap(this::parseRaw);
    }

    default <C> Either<UserError, C> deserializeFromFile(
        String filePath,
        Class<C> clazz
    ) {
        return IOExt.readFileContent(filePath)
            .flatMap(s -> deserializeFromString(s, clazz))
            .flatMap(a -> getValidator().validate(a));
    }

    default <C> JavaType getType(Class<C> cClass) {
        return getObjectMapper().getTypeFactory().constructType(cClass);
    }

    default <C> JavaType getCollectionType(Class<C> cClass) {
        return getObjectMapper().getTypeFactory().constructCollectionType(java.util.List.class, cClass);
    }

    default <C> JavaType getMapType(Class<C> cClass) {
        return getObjectMapper().getTypeFactory().constructMapType(java.util.Map.class, String.class, cClass);
    }

    default <T> Either<UserError, List<T>> deserializeToList(String s, Class<T> tClass) {
        return TryExt.get(
            () -> getObjectMapper().<java.util.ArrayList<T>>readValue(s, getCollectionType(tClass)),
            "Can't deserialize to List from json string"
        ).map(List::ofAll);
    }

    default <T> Either<UserError, HashMap<String, T>> deserializeToMap(String s, Class<T> tClass) {
        return TryExt.get(
            () -> getObjectMapper().<java.util.Map<String, T>>readValue(s, getMapType(tClass)),
            "Can't deserialize to Map from json string"
        ).map(HashMap::ofAll);
    }

    default <C> Either<UserError, C> deserializeFromString(String s, Class<C> clazz) {
        return TryExt.get(
            () -> getObjectMapper().readValue(s, clazz),
            "Can't deserialize from json node"
        )
        .flatMap(a -> getValidator().validate(a));
    }

    default <C> Either<UserError, C> deserializeFromBase64(String s, Class<C> clazz) {
        return parseBase64(s)
            .flatMap(j -> deserializeFromNode(j, clazz));
    }

    default <C> Either<UserError, C> deserializeFromNode(JsonNode json, Class<C> clazz) {
        return TryExt.get(
            () -> getObjectMapper().treeToValue(json, clazz),
            "Can't deserialize from json node"
        ).flatMap(a -> getValidator().validate(a));
    }

    default <C, N extends JsonNode> Either<UserError, List<C>> deserializeList(
        Iterable<N> list,
        Class<C> clazz,
        JacksonModule.IJsonTransformer transform
    ) {
        return ListExt
            .applyToEach(list, e -> {
                val input = transform.apply(e);
                return deserializeFromNode(
                    input,
                    clazz
                );
            }, "Deserialize list");
    }

    default Either<UserError, JsonNode> mapToYamlAndParse(Map<String, String> map) {
        return parseRaw(
            map
                .map(t -> String.format("%s: %s", t._1, t._2))
                .mkString("\n")
        );
    }

}
