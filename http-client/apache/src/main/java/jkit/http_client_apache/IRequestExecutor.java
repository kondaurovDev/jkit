package jkit.http_client_apache;

import io.vavr.CheckedFunction1;
import io.vavr.control.Either;
import jkit.core.ext.StreamExt;
import jkit.http_client_core.HttpRequest;
import jkit.http_client_core.HttpResponse;
import jkit.http_client_core.JKitHttpClient;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import lombok.val;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;

import java.util.stream.Stream;

interface IRequestExecutor extends
    JKitHttpClient.IRequestExecutor<HttpUriRequest, org.apache.http.HttpResponse>
{

    HttpClient getHttpClient();

    default CheckedFunction1<HttpUriRequest, org.apache.http.HttpResponse> getRequestExecutor() {
        return r -> getHttpClient().execute(r);
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

        val


        val builder = RequestBuilder
            .create(request.getMethod())
            .setUri(request.getUrl());

        if (request.getEntity() != null)
            builder.setEntity(new ByteArrayEntity(request.getEntity()));

        request
            .getHeaders()
            .forEach(builder::addHeader);

        return builder.build();

    }
}
