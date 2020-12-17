package jkit.validate;

import io.vavr.collection.List;
import io.vavr.control.Try;
import jkit.core.JKitValidate;
import lombok.*;

import jkit.core.ext.*;
import javax.validation.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Validator implements JKitValidate.IValidator {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    javax.validation.Validator validator = validatorFactory.getValidator();

    public <A> Try<A> validate(
        A obj, String... fields
    ) {

        if (obj == null) {
            return Try.failure(new Error("Can't validate null"));
        }

        Try<List<ConstraintViolation<A>>> errors;

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
                    return Try.failure(
                        new Error(String.format(
                            "Validation errors(%s): %s",
                            obj.getClass().getName(),
                            msg
                        ))
                    );
                } else {
                    return Try.success(obj);
                }
            });

    }

}
