package jkit.entry;

import akka.http.javadsl.model.HttpRequest;
import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.Tuple;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jkit.core.model.UserError;
import jkit.core.ext.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "create")
public class HttpRequestFull {

    io.vavr.collection.Map queryParams;
    JsonNode headers;
    JsonNode cookie;
    JsonNode payload;

    public static HttpRequestFull create(
        HttpRequest httpRequest,
        String body
    ) {
        val queryParams = json
            .toJsonNode(httpRequest.getUri().query().toMap())
            .get();

        val headers = json
            .toJsonNode(getHeaders(httpRequest))
            .get();

        var payload = json.parseRaw(body).get();

        Option<String> cookieVal = jsonDSL.getPathOpt(
            headers,
            Headers.cookie
        ).map(JsonNode::asText);

        IOExt.log(l -> l.debug("User cookie:" + cookieVal));

        var cookie = json
            .toJsonNode(getCookiesFromString(cookieVal))
            .mapLeft(e -> e.withError("Can't read cookies"))
            .get();

        return new HttpRequestFull(
            queryParams,
            headers,
            cookie,
            payload
        );

    }

    static Map<String, String> getHeaders(HttpRequest httpRequest) {
        return VavrExt
            .createList(httpRequest.getHeaders())
            .toMap(t -> Tuple.of(t.name().toLowerCase(), t.value()))
            .toJavaMap();
    }

    static Map<String, String> getCookiesFromString(Option<String> cookieStr) {
        return cookieStr
            .map(cookie -> VavrExt
                .createList(cookie.split(";"))
                .toMap(t -> {
                    int i = t.indexOf("=");
                    return Tuple.of(
                        t.substring(0,i).trim().toLowerCase(),
                        t.substring(i + 1).trim()
                    );
                })
            ).getOrElse(VavrExt.createEmptyMap()).toJavaMap();
    }

    public Either<UserError, String> getParam(String key, JsonNode params, String domain) {
        return jsonDSL.getPath(
            params,
            key,
            v -> Either.right(v.asText())
        ).mapLeft(e -> UserError
            .create(String.format("Can't get %s param", domain))
            .withErrors(List.of(e))
        );
    }

    public Either<UserError, String> getQueryParam(String key) {
        return getParam(key, queryParams, "query");
    }

    public Either<UserError, String> getHeaderParam(String key) {
        return getParam(key, headers, "header");
    }

    public <B> Either<UserError, B> getQueryParams(Class<B> clazz) {

        return json
            .deserialize(queryParams, clazz);

    }

}
