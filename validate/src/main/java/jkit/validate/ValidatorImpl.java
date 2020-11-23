package jkit.validate;

import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.model.UserError;
import lombok.EqualsAndHashCode;
import lombok.Value;

import jkit.core.ext.*;
import javax.validation.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ValidatorImpl implements jkit.core.iface.Validator {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    javax.validation.Validator validator = validatorFactory.getValidator();

    public <A> Either<UserError, A> validate(
        A obj, String... fields
    ) {

        if (obj == null) {
            return Either.left(UserError.create("Can't validate null"));
        }

        Either<UserError, List<ConstraintViolation<A>>> errors;

        if (fields.length == 0) {
            errors = TryExt
                .get(() -> validator.validate(obj), "err")
                .map(List::ofAll);
        } else {
            errors = ListExt
                .applyToEach(
                    List.of(fields),
                    v -> TryExt.get(() -> validator.validateProperty(obj, v), "validate property"),
                    "validate object"
                )
                .map(lst -> lst.flatMap(v -> v));
        }

        return errors
            .flatMap(err -> {
                if (err.nonEmpty()) {
                    String msg = err
                        .map(e -> e.getPropertyPath() + "(" + e.getMessage() + ")")
                        .mkString(", ");
                    return Either.left(
                        UserError.create(String.format(
                            "Validation errors(%s): %s",
                            obj.getClass().getName(),
                            msg
                        ))
                    );
                } else {
                    return Either.right(obj);
                }
            });

    }

}
