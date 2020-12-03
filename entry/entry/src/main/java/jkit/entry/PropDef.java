package jkit.entry;

import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.ext.ListExt;
import jkit.core.iface.Entry;
import jkit.core.model.UserError;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PropDef<A> implements Entry.IPropDef<A> {

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

    UserError wrongType(Object obj, String expected) {
        return UserError.create(String.format(
            "'%s' not a %s but %s",
            name,
            expected,
            obj.getClass().getSimpleName()
        ));
    }

    public Either<UserError, ?> validateObj(Object obj) {

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

    public Either<UserError, List<A>> validateList(Object object) {

        if (!(object instanceof java.util.List<?>)) {
            return Either.left(UserError.create("Must be list"));
        }

        return ListExt.applyToEach(
            (java.util.List<?>)object,
            v -> {
                if (paramClass.isInstance(v)) {
                    return Either.left(UserError.create("Wrong element type: " + v.getClass().getSimpleName()));
                }
                return Either.right(paramClass.cast(v));
            },
            "to list",
            true
        );

    }

}