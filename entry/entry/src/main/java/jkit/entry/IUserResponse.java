package jkit.entry;

import akka.http.javadsl.model.ContentType;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.headers.Location;
import io.vavr.control.Either;
import jkit.entry.ResponseHolder;
import jc.core.model.UserError;
import jc.core.ext.*;

import static jc.core.CoreDefault.*;

public interface IUserResponse {

    static IApi.IResponse plainText(String text, Integer status) {
        return new ResponseHolder()
            .response(Response.text.getHttpResponse(text, status));
    }

    static IApi.IResponse yaml(Object resp) {
        return yamlResponse(resp, 200)
            .mapLeft(e -> e.withError("Create yaml response"))
            .getOrElseThrow((e) -> new Error(e.toString()));
    }

    static IApi.IResponse json(Object resp) {
        return json(resp, 200);
    }

    static IApi.IResponse json(Object resp, Integer status) {
        return jsonResponse(resp, status)
            .mapLeft(e -> e.withError("Create json response"))
            .getOrElseThrow(UserError::toError);
    }

    static Either<UserError, IApi.IResponse> jsonResponse(Object obj, int code) {
        return toResponse(
            json.toJsonString(obj),
            Response.json,
            code
        );
    }

    static Either<UserError, IApi.IResponse> yamlResponse(Object obj, int code) {
        return toResponse(
            yaml.toJsonString(obj),
            Response.text,
            code
        );
    }

    static Either<UserError, IApi.IResponse> toResponse(
        Either<UserError, String> str,
        Response response,
        int status
    ) {
        return str
            .map(node -> new ResponseHolder()
                .response(response.getHttpResponse(node, status))
            );
    }

    static Either<UserError, IApi.IResponse> html(String html) {
        return html(html, 200);
    }

    static Either<UserError, IApi.IResponse> html(String html, Integer status) {
        return TryExt.get(
            () -> new ResponseHolder()
                .response(Response.html.getHttpResponse(html, status)),
            "create html response"
        );
    }

    static Either<UserError, ResponseHolder> redirect(String url) {

        IOExt.log(l -> l.debug("create redirect to " + url));
        return TryExt.get(
            () -> HttpResponse
                    .create()
                    .withStatus(StatusCodes.TEMPORARY_REDIRECT)
                    .addHeader(Location.create(url))
                , "create http response"
        ).map(r -> new ResponseHolder().response(r));

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
