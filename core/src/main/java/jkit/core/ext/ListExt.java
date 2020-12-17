package jkit.core.ext;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import lombok.val;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

public interface ListExt {

    static <A> List<A> create(Iterator<A> it) {
        ArrayList<A> list = new ArrayList<>();
        it.forEachRemaining(list::add);
        return List.ofAll(list);
    }

    static <A, R> Try<List<R>> applyToEach(
        Iterable<A> source,
        Function<A, Try<R>> handle,
        String errorMsg
    ) {
        return applyToEach(
            source,
            handle,
            errorMsg,
            false
        );
    }

    static <A, R> Try<List<R>> applyToEach(
        Iterable<A> source,
        Function<A, Try<R>> handle,
        String errorMsg,
        Boolean failOnFirst
    ) {

        ArrayList<R> success = new ArrayList<>();
        ArrayList<Throwable> errors = new ArrayList<>();

        source.forEach(i -> {
            if (failOnFirst && !errors.isEmpty()) {
                return;
            }
            handle.apply(i).fold(
                errors::add,
                r -> {
                    if (r == null) return null;
                    success.add(r);
                    return null;
                }
            );
        });

        if (errors.isEmpty()) {
            return Try.success(List.ofAll(success));
        } else {
            return Try.failure(new Error(errorMsg, errors.get(0)));
        }

    }

    static List<Tuple2<Integer, Object>> from(Stream<?> stream, Integer offset) {
        final Stream<Tuple2<Integer, Object>> list = stream
            .zipWithIndex((p, i) -> Tuple.of(i + 1 + offset, p));
        return List.ofAll(list);
    }

    static <A> List<A> merge(
        Iterable<? extends A> listA,
        Iterable<? extends A> listB
    ) {
        val res = new ArrayList<A>();
        listA.forEach(res::add);
        listB.forEach(res::add);
        return List.ofAll(res);
    }

}
