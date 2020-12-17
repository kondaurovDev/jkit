package jkit.core.ext;

import io.vavr.control.Try;

public interface EnumExt {

    static <A extends Enum<A>> Try<A> getByName(
        String name,
        Class<A> clazz
    ) {
        return TryExt.get(
            () -> Enum.valueOf(clazz, name.toUpperCase()),
            String.format("Unknown enum %s", name)
        );
    }

}
