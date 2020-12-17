package jkit.akka_http.util;

import akka.http.javadsl.model.*;
import akka.http.javadsl.model.headers.Location;
import io.vavr.control.Either;
import jkit.core.JKitData;
import jkit.core.model.JKitError;
import jkit.core.ext.*;

public interface IResponseFactory {

    JKitData.IObjMapperMain<?, ? extends JKitData.IObjMapper<?>> getObjMapperMain();

    default HttpResponse plainText(String text, Integer status) {
        return Response.text.getHttpResponse(text, status);
    }

    default HttpResponse yamlResponseOrThrow(Object resp) {
        return yamlResponse(resp, 200)
            .mapLeft(e -> e.withError("Create yaml response"))
            .getOrElseThrow(e -> new java.lang.Error(e.toString()));
    }

    default HttpResponse jsonResponse(Object resp) {
        return jsonResponseOrThrow(resp, 200);
    }

    default HttpResponse jsonResponseOrThrow(Object resp, Integer status) {
        return jsonResponse(resp, status)
            .mapLeft(e -> e.withError("Create json response"))
            .getOrElseThrow(JKitError::toError);
    }

    default Either<JKitError, HttpResponse> jsonResponse(Object obj, int code) {
        return toResponse(
            getObjMapperMain().getJson().serialize(obj),
            Response.json,
            code
        );
    }

    default Either<JKitError, HttpResponse> yamlResponse(Object obj, int code) {
        return toResponse(
            getObjMapperMain().getJson().serialize(obj),
            Response.text,
            code
        );
    }

    default Either<JKitError, HttpResponse> toResponse(
        Either<JKitError, String> str,
        Response response,
        int status
    ) {
        return str
            .map(node -> response.getHttpResponse(node, status));
    }

    default Either<JKitError, HttpResponse> html(String html) {
        return html(html, 200);
    }

    default Either<JKitError, HttpResponse> html(String html, Integer status) {
        return Either.right(Response.html.getHttpResponse(html, status));
    }

    default Either<JKitError, HttpResponse> redirect(String url) {
        return TryExt.get(
            () -> HttpResponse
                .create()
                .withStatus(StatusCodes.TEMPORARY_REDIRECT)
                .addHeader(Location.create(url)),
            "create http response"
        );

    }

    enum Response {
        json(ContentTypes.APPLICATION_JSON),
        text(ContentTypes.TEXT_PLAIN_UTF8),
        stream(ContentTypes.APPLICATION_OCTET_STREAM),
        html(ContentTypes.TEXT_HTML_UTF8);

        final ContentType contentType;

        Response(ContentType contentType) {
            this.contentType = contentType;
        }

        HttpResponse getHttpResponse(String body, Integer status) {
            return HttpResponse
                .create()
                .withEntity(contentType, body.getBytes())
                .withStatus(status);
        }

    }

}
