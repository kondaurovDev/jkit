package jkit.core.ext;

import io.vavr.CheckedFunction0;
import io.vavr.CheckedRunnable;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jkit.core.model.UserError;
import lombok.val;

import java.util.concurrent.CompletableFuture;

public interface TryExt {

    static <R> Either<UserError, R> await(
        CompletableFuture<R> future
    ) {
        val res = Future.fromCompletableFuture(future).await();
        if (res.isSuccess()) {
            return Either.right(res.get());
        } else {
            return Either.left(UserError.create("Await future", res.getCause().get()));
        }
    }

    static Either<UserError, Void> getAndVoid(
        CheckedRunnable lambda,
        String errorMsg
    ) {
        return get(() -> {
            lambda.run();
            return null;
        }, errorMsg);
    }

    static <R> Either<UserError, R> get(
        CheckedFunction0<R> lambda,
        String errorMsg
    ) {

        return Try
            .of(lambda)
            .toEither()
            .mapLeft(e ->
                UserError.create(errorMsg, e)
            );

    }

    static <R> R getOrThrow(CheckedFunction0<R> lambda, String errorMsg) {
        return Try
            .of(lambda)
            .getOrElseThrow(e -> new Error(errorMsg, e));
    }

    static <A> Either<UserError, A> assertStmt(
        A value,
        Function1<A, Boolean> cond,
        String errorMsg
    ) {
        if (cond.apply(value)) {
           return Either.right(value);
        } else {
           return Either.left(UserError.create(errorMsg));
        }
    }

    static <R> Either<UserError, R> error(String msg) {

        return Either.left(UserError.create(msg));

    }

}
