package jkit.core;

import io.vavr.collection.List;
import io.vavr.control.Try;
import jkit.core.ext.MapExt;

import java.util.Map;

public interface JKitData {

    interface IObjMapper<A> extends IParser<A> {
        Try<String> serialize(Object object);
        <C> Try<C> convert(Object obj, Class<C> clazz, String... fields);
        <C> Try<C> deserialize(Object input, Class<C> clazz);
        Try<Map<String, Object>> objToMap(Object node);
        <T> Try<List<T>> deserializeToList(String s, Class<T> tClass);
        <T> Try<Map<String, T>> deserializeToMap(String s, Class<T> tClass);
        <C> Try<C> deserializeFromString(String s, Class<C> clazz);
        <C> Try<C> deserializeFromNode(A json, Class<C> clazz);
    }

    interface IParser<A> {
        Try<A> parseRaw(String raw);
        Try<A> parseBase64(String encoded);
    }

    interface IObjMapperMain<J, A extends IObjMapper<J>> {
        IObjMapper<J> getJson();
        A getYml();

        Try<java.util.Map<String, Object>> readPayload(
            String body,
            CorePredef.DataFormat dataFormat
        );

        default Try<J> mapToYmlAndParse(
            Map<String, String> input
        ) {
            return getYml().parseRaw(MapExt.map2yaml(input));
        }

    }

}
