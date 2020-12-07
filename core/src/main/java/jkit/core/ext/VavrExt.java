package jkit.core.ext;

import io.vavr.Function0;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jkit.core.model.UserError;

public interface VavrExt {

    static <K, V> HashMap<K, V> createEmptyMap() {
        return HashMap.empty();
    }

    static <A> List<A> createList(
        Iterable<A> elems
    ) {
        return List.ofAll(elems);
    }

    static <A> List<A> createList(
        A[] elems
    ) {
        return List.of(elems);
    }

    static <A> Either<UserError, A> checkNull(A opt, String error) {
        if (opt == null) return Either.left(UserError.create(error));
        return Either.right(opt);
    }

    static <A> Either<UserError, A> get(Option<A> opt, String name) {
        return opt.toEither(UserError.create("Missing value: " + name));
    }

    static Either<UserError, Integer> parseInt(String s) {
        return TryExt.get(() -> Integer.parseInt(s), "to int");
    }

    static <A> Either<UserError, A> getOrDefault(
        A value,
        Function0<Either<UserError, A>> getDefault
    ) {
        if (value != null) return Either.right(value);
        return getDefault.apply();
    }

}
