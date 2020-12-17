package jkit.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.control.Try;
import jkit.core.ext.*;
import jkit.core.JKitData;

import java.util.Map;

public interface IDeserialize extends JKitData.IParser<JsonNode>, IObjMapper {

    default Try<JsonNode> parseRaw(String raw) {
        return TryExt.get(
            () -> getObjectMapper().readTree(raw),
            "Parse json string"
        );
    }

    default Try<JsonNode> parseBase64(String encoded) {
        return StringExt
            .decodeBase64(encoded)
            .flatMap(this::parseRaw);
    }

    default <C> Try<C> deserializeFromFile(
        String filePath,
        Class<C> clazz
    ) {
        return IOExt.readFileContent(filePath)
            .flatMap(s -> deserializeFromString(s, clazz))
            .flatMap(a -> getValidator().validate(a));
    }

    default <C> JavaType getCustomType(Class<C> cClass) {
        return getObjectMapper().getTypeFactory().constructType(cClass);
    }

    default <C> JavaType getCollectionType(Class<C> cClass) {
        return getObjectMapper().getTypeFactory().constructCollectionType(java.util.List.class, cClass);
    }

    default <C> JavaType getMapType(Class<C> cClass) {
        return getObjectMapper().getTypeFactory().constructMapType(Map.class, String.class, cClass);
    }

    default <T> Try<List<T>> deserializeToList(String s, Class<T> tClass) {
        return TryExt
            .get(
                () -> getObjectMapper().<java.util.ArrayList<T>>readValue(s, getCollectionType(tClass)),
                "deserialize to List from json string"
            )
            .map(List::ofAll);
    }

    default <T> Try<Map<String, T>> deserializeToMap(String s, Class<T> tClass) {
        return TryExt
            .get(
                () -> getObjectMapper().<java.util.Map<String, T>>readValue(s, getMapType(tClass)),
                "deserialize to Map from json string"
            );
    }

    default <C> Try<C> deserializeFromString(String s, Class<C> clazz) {
        return TryExt.get(
            () -> getObjectMapper().readValue(s, clazz),
            "deserialize from string"
        )
        .flatMap(a -> getValidator().validate(a));
    }

    default <C> Try<C> deserializeFromBase64(String s, Class<C> clazz) {
        return this
            .parseBase64(s)
            .flatMap(j -> deserializeFromNode(j, clazz));
    }

    default <C> Try<C> deserializeFromNode(JsonNode json, Class<C> clazz) {
        return TryExt
            .get(
                () -> getObjectMapper().treeToValue(json, clazz),
                "Can't deserialize from json node"
            )
            .flatMap(a -> getValidator().validate(a));
    }

    default <C, N extends JsonNode> Try<List<C>> deserializeList(
        Iterable<N> list,
        Class<C> clazz,
        IFactory.IJsonTransformer transform
    ) {
        return ListExt
            .applyToEach(list, e -> deserializeFromNode(
                transform.apply(e),
                clazz
            ), "Deserialize list");
    }

}
