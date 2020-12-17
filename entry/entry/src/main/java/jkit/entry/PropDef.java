package jkit.entry;

import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jkit.core.ext.ListExt;
import jkit.core.JKitEntry;
import jkit.core.model.JKitError;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PropDef<A> implements JKitEntry.IPropDef<A> {

    @EqualsAndHashCode.Include
    String name;
    Class<A> paramClass;
    Boolean isList;

    public static <A> PropDef<A> of(
        String name,
        Class<A> paramClass
    ) {
        return PropDef.of(
            name,
            paramClass,
            false
        );
    }

    public static <A> PropDef<A> list(
        String name,
        Class<A> aClass
    ) {
        return PropDef.of(
            name,
            aClass,
            true
        );
    }

    JKitError wrongType(Object obj, String expected) {
        return JKitError.of().withError(String.format(
            "'%s' not a %s but %s",
            name,
            expected,
            obj.getClass().getSimpleName()
        ));
    }

    public Try<Object> validateObj(Object obj) {

        if (Boolean.TRUE.equals(isList)) {
            if (obj instanceof java.util.List<?>) {
                return validateList(obj);
            } else {
                return Either.left(wrongType(obj, "List"));
            }
        }

        if (!paramClass.isInstance(obj))
            return Either.left(wrongType(obj, paramClass.getCanonicalName()));

        return Either.right(obj);

    }

    public Either<JKitError, List<A>> validateList(Object object) {

        if (!(object instanceof java.util.List<?>)) {
            return Either.left(JKitError.create("Must be list"));
        }

        return ListExt.applyToEach(
            (java.util.List<?>)object,
            v -> {
                if (paramClass.isInstance(v)) {
                    return Either.left(JKitError.create("Wrong element type: " + v.getClass().getSimpleName()));
                }
                return Either.right(paramClass.cast(v));
            },
            "to list",
            true
        );

    }

}