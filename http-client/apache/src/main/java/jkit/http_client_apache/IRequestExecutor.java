package jkit.http_client_apache;

import io.vavr.Function1;
import io.vavr.control.Either;
import jkit.core.ext.StreamExt;
import jkit.http_client.HttpRequest;
import jkit.http_client.HttpResponse;
import jkit.http_client.JKitHttpClient;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import lombok.val;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;

import java.util.stream.Stream;

interface IRequestExecutor extends
    JKitHttpClient.IRequestExecutor<HttpUriRequest, org.apache.http.HttpResponse>
{

    HttpClient getHttpClient();

    default Function1<HttpUriRequest, Either<UserError, org.apache.http.HttpResponse>> getRequestExecutor() {
        return r -> TryExt.get(() -> getHttpClient().execute(r), "Execute http request");
    }

    default Either<UserError, HttpResponse> castResponse(
        org.apache.http.HttpResponse response
    ) {
        return TryExt
            .get(() -> response.getEntity().getContent(), "read input stream")
            .flatMap(StreamExt::readAllBytes)
            .map(body -> HttpResponse.create(
                response.getStatusLine().getStatusCode(),
                response.getStatusLine().getReasonPhrase(),
                body,
                StreamExt.streamToMap(
                    Stream.of(response.getAllHeaders()),
                    NameValuePair::getName,
                    NameValuePair::getValue
                )
            ))
            .mapLeft(e -> e.withError("Convert http response (apache)"));
    }

    default Either<UserError, HttpUriRequest> castRequest(HttpRequest request) {

        return request
            .getUrl().createURI()
            .flatMap(uri -> {
                val builder = RequestBuilder
                    .create(request.getMethod())
                    .setUri(uri);

                if (request.getEntity() != null) {
                    val e = TryExt.get(request.getEntity(), "read entity");
                    if (e.isLeft()) return Either.left(e.getLeft());
                    builder.setEntity(new InputStreamEntity(e.get()));
                }

                request
                    .getHeaders()
                    .forEach(p -> builder.addHeader(p.getFirst(), p.getSecond()));

                return Either.right(builder.build());
            });

    }
}
