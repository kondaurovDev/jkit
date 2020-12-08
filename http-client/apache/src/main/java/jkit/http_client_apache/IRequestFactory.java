package jkit.http_client_apache;

import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import jkit.http_client_core.JKitHttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;

interface IRequestFactory extends JKitHttpClient.IRequestFactory<HttpUriRequest> {

    default Either<UserError, URIBuilder> createUriBuilder(List<String> urlParts) {

        return TryExt.get(
            () -> new URIBuilder(urlParts.mkString("/")),
            "Create url builder"
        );

    }

    default Either<UserError, HttpGet> createGetRequest(String uri) {
        return createUri(uri).map(HttpGet::new);
    }

    default Either<UserError, HttpPost> createPostRequest(String uri) {
        return createUri(uri).map(HttpPost::new);
    }

    default Either<UserError, HttpDelete> createDeleteRequest(String uri) {
        return createUri(uri).map(HttpDelete::new);
    }

    default Either<UserError, HttpPut> createPutRequest(String uri) {
        return createUri(uri).map(HttpPut::new);
    }

    default Either<UserError, URI> createUri(String uri) {
        return TryExt.get(
            () -> new URI(uri),
            "create uri"
        );
    }
    
}
