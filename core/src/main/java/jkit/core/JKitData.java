package jkit.core;

import io.vavr.control.Either;
import jkit.core.ext.MapExt;
import jkit.core.model.UserError;

import java.util.Map;

public interface JKitData {

    interface IObjMapper<A> extends IParser<A> {
        Either<UserError, String> serialize(Object object);
        <C> Either<UserError, C> convert(Object obj, Class<C> clazz, String... fields);
        <C> Either<UserError, C> deserialize(Object input, Class<C> clazz);
        Either<UserError, Map<String, Object>> objToMap(Object node);
    }

    interface IParser<A> {
        Either<UserError, A> parseRaw(String raw);
        Either<UserError, A> parseBase64(String encoded);
    }

    interface IObjMapperMain<J, A extends IObjMapper<J>> {
        A getJson();
        A getYml();

        Either<UserError, java.util.Map<String, Object>> readPayload(
            String body,
            CorePredef.DataFormat dataFormat
        );

        default Either<UserError, J> mapToYmlAndParse(
            Map<String, String> input
        ) {
            return getYml().parseRaw(MapExt.map2yaml(input));
        }

    }

}
