package jkit.entry;

import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.ext.ListExt;
import jkit.core.model.UserError;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommandParam<A> implements IApi.Name {

    @EqualsAndHashCode.Include
    String name;
    Class<A> aClass;
    Boolean isList;

    public static <A> CommandParam<A> of(
        String name,
        Class<A> aClass
    ) {
        return CommandParam.of(
            name,
            aClass,
            false
        );
    }

    public static <A> CommandParam<A> list(
        String name,
        Class<A> aClass
    ) {
        return CommandParam.of(
            name,
            aClass,
            true
        );
    }

    public Either<UserError, ?> processInput(Object obj) {

        if (Boolean.TRUE.equals(isList)) {
            if (obj instanceof java.util.List<?>) {
                return getList(obj);
            } else {
                return Either.left(UserError.create(String.format(
                    "'%s' not a list but %s",
                    name,
                    obj.getClass().getSimpleName()
                )));
            }
        }

        return json.convert(obj, aClass);

    }

    public Either<UserError, List<A>> getList(Object object) {

        if (!(object instanceof java.util.List<?>)) {
            return Either.left(UserError.create("Must be list"));
        }

        return ListExt.applyToEach(
            (java.util.List<?>)object,
            v -> {
                if (aClass.isInstance(v)) {
                    return Either.left(UserError.create("Wrong element type: " + v.getClass().getSimpleName()));
                }
                return json.convert(v, aClass);
            },
            "to list",
            true
        );

    }

}