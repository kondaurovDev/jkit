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

        return EntryGlobal.$.getObjectMapper()
            .flatMap(mapper -> mapper.convert(obj, paramClass));

    }

    public Either<UserError, List<A>> getList(Object object) {

        if (!(object instanceof java.util.List<?>)) {
            return Either.left(UserError.create("Must be list"));
        }

        return ListExt.applyToEach(
            (java.util.List<?>)object,
            v -> {
                if (paramClass.isInstance(v)) {
                    return Either.left(UserError.create("Wrong element type: " + v.getClass().getSimpleName()));
                }
                return EntryGlobal.$.getObjectMapper()
                    .flatMap(mapper -> mapper.convert(v, paramClass));
            },
            "to list",
            true
        );

    }

}