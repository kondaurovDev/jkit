package com.jkit.validate;

import io.vavr.collection.List;
import io.vavr.control.Try;
import com.jkit.core.JKitValidate;
import lombok.*;

import com.jkit.core.ext.*;
import javax.validation.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Validator implements JKitValidate.IValidator {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    javax.validation.Validator validator = validatorFactory.getValidator();

    static String const2str(ConstraintViolation<?> err) {
        return String.format(
            "%s (%s)",
            err.getPropertyPath(),
            err.getMessage()
        );
    }

    public <A> Try<A> validate(
        A obj, String... fields
    ) {

        if (obj == null) {
            return Try.failure(new Error("Can't validate null"));
        }

        Try<List<ConstraintViolation<?>>> errors;

        if (fields.length == 0) {
            errors = TryExt
                .get(() -> validator.validate(obj), "err")
                .map(List::ofAll);
        } else {
            errors = ListExt
                .applyToEach(
                    List.of(fields),
                    f -> TryExt.get(() -> validator.validateProperty(obj, f), "validate property"),
                    "validate object"
                )
                .map(lst -> lst.flatMap(v -> v));
        }

        return errors
            .flatMap(err -> {
                if (!err.isEmpty()) {
                    return Try.failure(
                        new Error(String.format(
                            "Validation errors(%s): %s",
                            obj.getClass().getName(),
                            err.map(Validator::const2str).mkString(", ")
                        ))
                    );
                } else {
                    return Try.success(obj);
                }
            });

    }

}
