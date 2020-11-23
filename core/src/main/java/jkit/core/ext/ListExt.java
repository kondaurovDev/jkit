package jkit.core.ext;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Either;
import jkit.core.model.UserError;
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

    static <A, R> Either<UserError, List<R>> applyToEach(
        Iterable<A> source,
        Function<A, Either<UserError, R>> handle,
        String errorMsg
    ) {
        return applyToEach(
            source,
            handle,
            errorMsg,
            false
        );
    }

    static <A, R> Either<UserError, List<R>> applyToEach(
        Iterable<A> source,
        Function<A, Either<UserError, R>> handle,
        String errorMsg,
        Boolean failOnFirst
    ) {

        ArrayList<R> success = new ArrayList<>();
        ArrayList<UserError> errors = new ArrayList<>();

        source.forEach(i -> {
            if (failOnFirst && !errors.isEmpty()) {
                return;
            }
            handle.apply(i).fold(
                errors::add,
                success::add
            );
        });

        if (errors.isEmpty()) {
            return Either.right(List.ofAll(success));
        } else {
            return Either.left(UserError
                .create(errorMsg)
                .withErrors(errors)
            );
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
