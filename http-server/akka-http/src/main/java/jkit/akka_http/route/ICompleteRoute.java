package jkit.akka_http.route;

import akka.NotUsed;
import akka.http.javadsl.marshalling.sse.EventStreamMarshalling;
import akka.http.javadsl.model.headers.HttpCookiePair;
import akka.http.javadsl.model.sse.ServerSentEvent;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.ExceptionHandler;
import akka.http.javadsl.server.Route;
import akka.stream.javadsl.Source;
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import jkit.akka_http.AkkaPredef;
import jkit.akka_http.util.IResponseFactory;
import jkit.core.ext.*;
import lombok.*;

public interface ICompleteRoute extends IResponseFactory {

    AllDirectives d();

    default Route withExceptionHandling(
        Function0<Route> inner
    ) {
        return d()
            .handleExceptions(createExceptionHandler(), inner);
    }

    default ExceptionHandler createExceptionHandler() {
        return ExceptionHandler.newBuilder()
            .match(Throwable.class, x -> {
                IOExt.log(l -> l.error("Request error", x));
                return completeError(JKitError.create("Internal error"), 500);
            })
            .build();
    }

    default Route noCache(Function0<Route> inner) {
        return d().respondWithHeader(AkkaPredef.noCacheControl, inner::apply);
    }

    default Route completeError(String error, Integer code) {
        return d().extractUri(uri -> {
            val msg = String.format(
                "Request (%s) completed with an error: %s",
                uri.getPathString(),
                error
            );
            IOExt.log(l -> l.error(msg));
            return d().complete(plainText(error, code));
        });
    }

    default Route completeError(String error) {
        return withRight(
            jsonResponse(HashMap.of("error", error), 400),
            r -> d().complete(r)
        );
    }

    default Route completeHtml(String html, Integer status) {
        return withRight(html(html, status), r -> d().complete(r));
    }

    default Route completeJson(Object json, Integer status) {
        return d().complete(jsonResponseOrThrow(json, status));
    }

    default Route completeSuccess(Function0<Either<JKitError, ?>> factory) {
        return withRight(
            factory.apply().map(this::jsonResponse),
            r -> d().complete(r)
        );
    }

    default Route completeJson(Object json) {
        return d().complete(jsonResponse(json));
    }

    default Route completeSse(
        Source<ServerSentEvent, NotUsed> src
    ) {
        return this.d().completeOK(
            src,
            EventStreamMarshalling.toEventStream()
        );
    }

    default <A> Route withRight(
        Either<JKitError, A> tried,
        Function1<A, Route> handle
    ) {
        return tried.fold(
            userError -> completeError(userError.toString()),
            handle
        );
    }

    default Route withOptionalParam(
        String paramName,
        Function1<Option<String>, Route> inner
    ) {
        return d().parameterOptional(paramName,
            v -> inner.apply(Option.ofOptional(v))
        );
    }

    default Route withCookieOpt(
        String cookieName,
        Function1<Option<String>, Route> inner
    ) {
        return d().optionalCookie(cookieName, auth -> inner
            .apply(Option.ofOptional(auth).map(HttpCookiePair::value))
        );
    }
}
