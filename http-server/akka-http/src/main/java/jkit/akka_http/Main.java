package jkit.akka_http;

import akka.http.javadsl.model.HttpHeader;
import akka.http.javadsl.server.directives.SecurityDirectives;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import jkit.validate.Validator;
import lombok.val;

import jkit.jackson.JacksonMain;

public interface Main {

    AkkaExt.CreateRoute createRoute =
        d -> d.concat(
            d.pathEndOrSingleSlash(() -> d.complete("JKit http server")),
            d.path("product", () -> d.completeJson(HashMap.of("product", "macbook air"))),
            d.path("secured", () ->
                d.authenticateBasic(
                    "site",
                    (cred) -> cred
                        .filter(c -> c.verify("pwd"))
                        .map(SecurityDirectives.ProvidedCredentials::identifier),
                    u -> d.complete("Hello " + u)
                )
            ),
            d.path("greet", () -> d.get(() ->
                d.parameter("name", name ->
                    d.completeJson(HashMap.of("message", "Hello " + name))
                )
            )),
            d.path("echo", () ->
                d.withPayloadStringFromBody(payload ->
                    d.extractRequest(r ->
                        d.completeJson(
                            HashMap.of(
                                "your message", payload,
                                "headers", List.ofAll(r.getHeaders()).map(HttpHeader::name)
                            )
                        )
                    )
                )
            ),
            d.path("file", () ->
                d.extractRequestEntity(requestEntity -> {
                    val a = 1;
                    return d.complete("file upload");
                })
            )
        );

    static void main(String[] args) {
        val objMapper = JacksonMain.create(Validator.of());
        AkkaExt.buildAndListen(
            8080,
            "test",
            objMapper,
            createRoute
        );
    }

}
