package jkit.http_client;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.ext.IOExt;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.InputStream;

interface IReadResponse {

    HttpClient getHttpClient();

    default <A> Either<UserError, A> readContent(
        org.apache.http.HttpResponse response,
        Function1<InputStream, Either<UserError, A>> handle
    ) {
        return TryExt
            .get(() -> response.getEntity().getContent(), "get content")
            .flatMap(handle)
            .mapLeft(e -> e.withError("read http response entity"));
    }

    default Either<UserError, HttpResponse<String>> getStringResponse(
        HttpUriRequest req
    ) {

       return executeRequest(
            req,
            v -> readContent(v, IOExt::inputStreamToString)
       );

    }

    default Either<UserError, HttpResponse<String>> getJsonResponse(
        HttpUriRequest req
    ) {
       return getStringResponse(req);
    }

    @SuppressWarnings("Convert2MethodRef")
    default Either<UserError, HttpResponse<InputStream>> getStreamResponse(
        HttpUriRequest req
    ) {

       return executeRequest(
            req,
            r -> readContent(r, v -> Either.right(v))
       );

    }

    default <A> Either<UserError, HttpResponse<A>> executeRequest(
        HttpUriRequest req,
        Function1<org.apache.http.HttpResponse, Either<UserError, A>> handle
    ) {

       return TryExt
            .get(() -> getHttpClient().execute(req), "execute request")
            .flatMap(response -> handle.apply(response)
                .map(entity -> HttpResponse.create(
                    response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase(),
                    entity,
                    List
                        .of(response.getAllHeaders())
                        .map(h -> Tuple.of(
                            h.getName().toLowerCase(),
                            h.getValue()
                        ))
                ))
            ).mapLeft(e -> e.withError("execute http request"));

    }

}
