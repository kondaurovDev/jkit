package jkit.http_client_apache;

import io.vavr.control.Either;
import jkit.core.model.Pair;
import jkit.core.model.Url;
import jkit.http_client_core.HttpResponse;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientImplTest implements Deps {

    @Test
    void executeGetRequest() {

        val resp =
            httpClient.execute((request, ctx) -> request
                .method(ctx.methodPost)
                .url(Url.createUrl(url -> url
                    .base("http://localhost:8080/greet")
                    .queryParam(Pair.of("name", "jack"))
                ))
            );

        val body = resp.map(HttpResponse::getBodyString);

        assertTrue(resp.isRight());
        assertTrue(body.isRight());

    }

    @Test
    void executePostRequest() {
        val resp = httpClient.execute((request, ctx) ->
            request
                .method(ctx.methodPost)
                .url(Url.createUrl(url -> url
                    .base("http://localhost:8080/echo")
                    .queryParam(Pair.of("name", "jack"))
                ))
                .header(ctx.ctText)
                .entity("greeting!!!".getBytes())
        );

        val body = resp.map(HttpResponse::getBodyString);

        assertTrue(body.isRight());
        assertEquals(Either.right("{\"your message\":\"greeting!!!\"}"), body);
    }

    @Test
    void executeAuthBasicRequest() {
        val resp = httpClient.execute((request, ctx) ->
            request
                .method(ctx.methodPost)
                .url(Url.createUrl(url -> url
                    .base("http://localhost:8080/secured")
                ))
                .header(ctx.headerBasicAuth("Alex", "pwd"))
                .header(ctx.ctText)
                .entity("greeting!!!".getBytes())
        );

        val body = resp.map(HttpResponse::getBodyString);

        assertTrue(body.isRight());
        assertEquals(Either.right("{\"your message\":\"greeting!!!\"}"), body);
    }

}