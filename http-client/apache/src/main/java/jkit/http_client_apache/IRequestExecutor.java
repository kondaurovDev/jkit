package jkit.http_client_apache;

import io.vavr.control.Either;
import jkit.core.ext.StreamExt;
import jkit.http_client.HttpRequest;
import jkit.http_client.HttpResponse;
import jkit.http_client.JKitHttpClient;
import jkit.core.ext.TryExt;
import jkit.core.model.JKitError;
import lombok.val;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;

import java.util.stream.Stream;

interface IRequestExecutor extends
    JKitHttpClient<HttpUriRequest, org.apache.http.HttpResponse>
{

    HttpClient getHttpClient();

    default ExecuteRequest<HttpUriRequest, org.apache.http.HttpResponse> getRequestExecutor() {
        return r -> TryExt.get(() -> getHttpClient().execute(r), "Execute http request");
    }

    default Either<JKitError, HttpResponse> castResponse(
        org.apache.http.HttpResponse response
    ) {
        return TryExt
            .get(() -> response.getEntity().getContent(), "read input stream")
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

    default Either<JKitError, HttpUriRequest> castRequest(HttpRequest request) {

        return request
            .getUrl().createURI()
            .flatMap(uri -> {
                val builder = RequestBuilder
                    .create(request.getMethod())
                    .setUri(uri);

                if (request.getEntity() != null) {
                    val e = TryExt.get(request.getEntity(), "read entity");
                    if (e.isLeft()) return e.map(r -> null);
                    val entity = new ByteArrayEntity(e.get().getBody());
                    entity.setContentType(e.get().getContentType());
                    builder.setEntity(entity);
//                    TryExt
//                        .get(() -> new StringEntity("hey"), "")
//                        .forEach(entity -> {
//                            entity.setContentType("asd");
//                            builder.setEntity(entity);
//                        });
                }

                builder.setHeader("surname", "alex");

                builder.setVersion(HttpVersion.HTTP_1_1);

                request
                    .getHeaders()
                    .forEach(p -> builder.setHeader(p.getFirst(), p.getSecond()));

                return Either.right(builder.build());
            });

    }
}
