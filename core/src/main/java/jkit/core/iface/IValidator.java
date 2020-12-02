package jkit.core.iface;

import io.vavr.control.Either;
import jkit.core.model.UserError;

public interface IValidator {
    <A> Either<UserError, A> validate(A input, String... fields);
}
