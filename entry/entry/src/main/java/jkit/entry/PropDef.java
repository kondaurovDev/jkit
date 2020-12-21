package jkit.entry;

import com.jkit.core.JKitEntry;
import io.vavr.collection.List;
import io.vavr.control.Try;
import com.jkit.core.ext.ListExt;
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

    Error wrongType(Object obj, String expected) {
        return new Error(String.format(
            "'%s' not a %s but %s",
            name,
            expected,
            obj.getClass().getSimpleName()
        ));
    }

    public Try<A> validateObj(Object obj) {

        if (!paramClass.isInstance(obj))
            return Try.failure(wrongType(obj, paramClass.getCanonicalName()));

        return Try.success(paramClass.cast(obj));

    }

    public Try<List<A>> validateList(Object object) {

        if (!(object instanceof java.util.List<?>)) {
            return Try.failure(new Error("Must be list"));
        }

        return ListExt.applyToEach(
            (java.util.List<?>)object,
            v -> {
                if (paramClass.isInstance(v)) {
                    return Try.failure(new Error("Wrong element type: " + v.getClass().getSimpleName()));
                }
                return Try.success(paramClass.cast(v));
            },
            "to list",
            true
        );

    }

}