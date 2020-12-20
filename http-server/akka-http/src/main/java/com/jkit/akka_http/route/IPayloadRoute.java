package com.jkit.akka_http.route;

import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import io.vavr.Function1;
import io.vavr.control.Option;
import com.jkit.core.ext.*;

import java.util.Map;

public interface IPayloadRoute extends ICompleteRoute {

    default Route withPayloadFromBody(
        Function1<Map<String, ?>, Route> inner
    ) {
        return d().entity(
            Unmarshaller.entityToString(),
            s -> withSuccess(
                getObjMapper().deserializeToMap(s, Object.class),
                inner::apply
            )
        );
    }

    default Route withOptEncodedParams(
        String paramName,
        Function1<Option<Map<String, Object>>, Route> inner
    ) {
        return withOptionalParam(paramName, opt ->
            opt
                .map(s -> StringExt
                    .decodeBase64(s)
                    .flatMap(json ->
                        getObjMapper().deserializeToMap(json, Object.class)
                    )
                )
                .fold(
                    () -> inner.apply(Option.none()),
                    some -> withSuccess(some, s -> inner.apply(Option.some(s)))
                )
        );
    }

    default Route withQueryParams(
        Function1<Map<String, ?>, Route> inner
    ) {
        return d().parameterMap(inner::apply);
    }

    default Route withPayload(
        Function1<Map<String, ?>, Route> inner
    ) {
         return d().concat(
            d().get(() ->
                withOptEncodedParams(
                    "payload",
                    opt -> opt.fold(
                        () -> withQueryParams(inner),
                        inner
                    )
                )
            ),
            d().post(() ->
                withPayloadFromBody(inner)
            )
        );
    }

    default <A> Route withPayloadClass(
        Class<A> clazz,
        Function1<A, Route> inner
    ) {
        return withPayload(p -> withSuccess(
            getObjMapper().deserialize(p, clazz),
            inner
        ));
    }

}
