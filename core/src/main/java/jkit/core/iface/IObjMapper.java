package jkit.core.iface;

import io.vavr.collection.Map;
import io.vavr.control.Either;
import jkit.core.model.UserError;

public interface IObjMapper {
    Either<UserError, String> serialize(Object object);
    <C> Either<UserError, C> convert(Object obj, Class<C> clazz, String... fields);
    <C> Either<UserError, C> deserialize(Object input, Class<C> clazz);
    Either<UserError, Map<String, Object>> objToMap(Object node);
}
