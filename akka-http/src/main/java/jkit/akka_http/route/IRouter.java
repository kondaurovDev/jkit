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
import io.vavr.control.Either;
import io.vavr.control.Option;
import jkit.akka_http.AkkaPredef;
import jkit.core.ext.*;
import jkit.core.iface.IObjMapper;
import jkit.core.model.UserError;
import lombok.val;

public interface IRouter {

    IObjMapper getObjMapper();

    AllDirectives d();

    default Route withExceptionHandling(
        Function0<Route> inner
    ) {
        return d()
            .handleExceptions(createExceptionHandler(), inner);
    }

    default ExceptionHandler createExceptionHandler() {
        return ExceptionHandler.newBuilder()
            .match(Throwable.class, (x) -> {
                IOExt.log(l -> l.error("Request error", x));
                return completeError(UserError.create("Internal error"), 500);
            })
            .build();
    }

    default Route noCache(Function0<Route> inner) {
        return d().respondWithHeader(AkkaPredef.noCacheControl, inner::apply);
    }

    default Route completeError(UserError error, Integer code) {
        val err = error.toString();
        return d().extractUri(uri -> {
            val msg = String.format(
                "Request (%s) completed with an error: %s",
                uri.getPathString(),
                err
            );
            IOExt.log(l -> l.error(msg));
            return IUserResponse.plainText(err, code).getRoute(this);
        });
    }

    default Route completeError(String error) {
        return withRight(
            IUserResponse
                .jsonResponse(getObjMapper().newObj().put("error", error), 400)
                .map(r -> r.getRoute(this)),
            r -> r
        );
    }

    default Route completeHtml(String html, Integer status) {
        return withRight(IUserResponse.html(html, status), r -> r.getRoute(this));
    }

    default Route completeJson(Object json, Integer status) {
        return IUserResponse.json(json, status).getRoute(this);
    }

    default Route completeSuccess(Function0<Either<UserError, ?>> factory) {
        return withRight(
            factory.apply().map(IUserResponse::json),
            r -> r.getRoute(this)
        );
    }

    default Route completeJson(Object json) {
        return IUserResponse.json(json).getRoute(this);
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
        Either<UserError, A> tried,
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
