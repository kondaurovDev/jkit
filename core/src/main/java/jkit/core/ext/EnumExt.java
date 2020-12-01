package jkit.core.ext;

import io.vavr.control.Either;
import io.vavr.control.Try;
import jkit.core.model.UserError;

public interface EnumExt {

    static <A extends Enum<A>> Either<UserError, A> getByName(
        String name,
        Class<A> clazz
    ) {
        return Try.of(() -> Enum.valueOf(clazz, name.toUpperCase()))
            .toEither(UserError.create(String.format("Unknown enum %s", name)));
    }

}
