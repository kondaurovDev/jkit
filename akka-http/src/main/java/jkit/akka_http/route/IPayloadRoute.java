package jkit.akka_http.route;

import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import io.vavr.Function1;
import io.vavr.control.Option;
import jkit.core.CorePredef;
import jkit.core.ext.*;

import java.util.Map;

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
        Function1<CorePredef.DataFormat, Route> inner
    ) {
        return withOptionalParam("payloadFormat", opt ->
            opt.fold(
                () -> inner.apply(CorePredef.DataFormat.JSON),
                pt -> withRight(EnumExt.getByName(pt, CorePredef.DataFormat.class), inner)
            )
        );
    }

    default Route withQueryParams(
        Function1<String, Route> inner
    ) {
        return d().parameterMap(params ->
            withRight(
                getObjMapperMain()
                    .mapToYmlAndParse(params)
                    .flatMap(o -> getObjMapperMain().getYml().serialize(o)),
                inner
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
        Function1<Map<String, Object>, Route> inner
    ) {

        return withPayloadString(payload ->
            withPayloadFormat(df ->
                withRight(
                    getObjMapperMain()
                        .readPayload(
                            payload,
                            df
                        ),
                    inner
                )
            )
        );
    }

    default <A> Route withPayloadClass(
        Class<A> clazz,
        Function1<A, Route> inner
    ) {
        return withPayload(p -> withRight(
            getObjMapperMain().getJson().deserialize(p, clazz),
            inner
        ));
    }

}
