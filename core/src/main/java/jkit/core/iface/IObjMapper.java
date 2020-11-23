package jkit.core.iface;

import io.vavr.control.Either;
import jkit.core.model.UserError;

public interface IObjMapper {
    Either<UserError, String> serialize(Object object);
    <C> Either<UserError, C> convert(Object obj, Class<C> clazz, String... fields);
}
