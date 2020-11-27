package jkit.http_client;

import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;

interface IFactory {

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
