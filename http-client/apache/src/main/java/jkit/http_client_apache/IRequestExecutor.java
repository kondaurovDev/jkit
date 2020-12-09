package jkit.http_client_apache;

import io.vavr.CheckedFunction1;
import io.vavr.control.Either;
import jkit.core.ext.StreamExt;
import jkit.http_client_core.HttpResponse;
import jkit.http_client_core.JKitHttpClient;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.stream.Stream;

interface IRequestExecutor extends
    JKitHttpClient.IEntityFactory<HttpEntity>,
    JKitHttpClient.IRequestExecutor<HttpUriRequest, org.apache.http.HttpResponse>
{

    HttpClient getHttpClient();

    default Either<UserError, HttpResponse> turnResponse(
        org.apache.http.HttpResponse response
    ) {
        return TryExt.get(() -> HttpResponse.create(
                response.getStatusLine().getStatusCode(),
                response.getStatusLine().getReasonPhrase(),
                response.getEntity().getContent(),
                StreamExt.streamToMap(
                    Stream.of(response.getAllHeaders()),
                    NameValuePair::getName,
                    NameValuePair::getValue
                )
            ),
            "Convert http response (apache)"
        );
    }

    default CheckedFunction1<HttpUriRequest, org.apache.http.HttpResponse> getRequestExecutor() {
        return r -> getHttpClient().execute(r);
    }

}
