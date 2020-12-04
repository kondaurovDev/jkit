package jkit.core;

import io.vavr.control.Either;
import jkit.core.model.UserError;

public interface JKitValidate {

    interface IValidator {
        <A> Either<UserError, A> validate(A input, String... fields);
    }

}
