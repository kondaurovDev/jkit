package jkit.akka_http.route;

import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import jc.server.api.IApi;
import jkit.core.ext.*;
import jc.server.model.RequestPayload;

import static jc.core.CoreDefault.*;

public interface IPayloadRoute extends IRouter {

    default Route withPayloadStringFromBody(
        Function1<String, Route> inner
    ) {
        return d().entity(
            Unmarshaller.entityToString(), inner
        );
    }

    default Route withOptEncodedParam(
        String paramName,
        Function1<Option<String>, Route> inner
    ) {
        return withOptionalParam(paramName, opt ->
            opt
                .map(StringExt::decodeBase64)
                .fold(
                    () -> inner.apply(Option.none()),
                    some -> withRight(some, s -> inner.apply(Option.some(s)))
                )
        );
    }

    default Route withPayloadFormat(
        Function1<IApi.DataFormat, Route> inner
    ) {
        return withOptionalParam("payloadFormat", opt ->
            opt.fold(
                () -> inner.apply(IApi.DataFormat.json),
                pt -> withRight(EnumExt.getByName(pt, IApi.DataFormat.class), inner)
            )
        );
    }

    default Route withQueryParams(
        Function1<String, Route> inner
    ) {
        return d().parameterMap(params ->
            withRight(
                yaml.mapToYamlAndParse(HashMap.ofAll(params)),
                r -> inner.apply(r.toPrettyString())
            )
        );
    }

    default Route withPayloadString(
        Function1<String, Route> inner
    ) {
         return d().concat(
            d().get(() ->
                withOptEncodedParam(
                    "payload",
                    opt -> opt.fold(
                        () -> withQueryParams(inner),
                        inner
                    )
                )
            ),
            d().post(() ->
                withPayloadStringFromBody(inner)
            )
        );
    }

    default Route withPayload(
        Function1<RequestPayload, Route> inner
    ) {

        return withPayloadString(payload ->
            withPayloadFormat(df ->
                inner.apply(RequestPayload.of(
                    payload, df
                ))
            )
        );
    }

    default <A> Route withPayloadClass(
        Class<A> clazz,
        Function1<A, Route> inner
    ) {
        return withPayload(p -> withRight(p.deserialize(clazz), inner));
    }

}
