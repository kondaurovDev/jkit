package ru.ak.jkit.jkit_core.ext;

import io.vavr.API;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedRunnable;
import io.vavr.Function1;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;
import lombok.val;

import java.util.concurrent.CompletableFuture;

import static io.vavr.API.$;

public interface TryExt {

    static <R> Try<R> await(
        CompletableFuture<R> future
    ) {
        val res = Future.fromCompletableFuture(future).await();
        if (res.isSuccess()) {
            return res.toTry();
        } else {
            return Try.failure(new Error(
                "Await future",
                res.getCause().get()
            ));
        }
    }

    static Try<Void> getAndVoid(
        CheckedRunnable lambda,
        String errorMsg
    ) {
        return get(() -> {
            lambda.run();
            return null;
        }, errorMsg);
    }

    @SuppressWarnings("unchecked")
    static <R> Try<R> get(
        CheckedFunction0<R> lambda,
        String errorMsg
    ) {

        return Try
            .of(lambda)
            .mapFailure(API.Case($(), e -> new Error(errorMsg, e)));

    }

    static <R> R getOrThrow(CheckedFunction0<R> lambda, String errorMsg) {
        return get(lambda, errorMsg)
            .getOrElseThrow((e) -> (Error)e);
    }

    static <A> Try<A> assertStmt(
        A value,
        Function1<A, Boolean> cond,
        String errorMsg
    ) {
        if (cond.apply(value)) {
           return Try.success(value);
        } else {
           return Try.failure(new Error(errorMsg, null));
        }
    }

}
