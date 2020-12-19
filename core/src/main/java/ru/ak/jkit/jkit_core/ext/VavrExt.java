package ru.ak.jkit.jkit_core.ext;

import io.vavr.Function0;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;

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

    static <A> Try<A> checkNull(A opt, String error) {
        if (opt == null) return Try.failure(new Error(error, null));
        return Try.success(opt);
    }

    static <A> Try<A> get(Option<A> opt, String name) {
        return opt.fold(
            () -> Try.failure(new Error("Missing value: " + name, null)),
            Try::success
        );
    }

    static Try<Integer> parseInt(String s) {
        return TryExt.get(() -> Integer.parseInt(s), "to int");
    }

    static <A> Try<A> getOrDefault(
        A value,
        Function0<Try<A>> getDefault
    ) {
        if (value != null) return Try.success(value);
        return getDefault.apply();
    }

}
