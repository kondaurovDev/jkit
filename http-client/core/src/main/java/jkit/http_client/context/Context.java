package jkit.http_client.context;

import io.vavr.Function1;
import jkit.core.model.Pair;
import jkit.core.model.Url;
import jkit.http_client.HttpRequest;

public interface Context extends IHeader, IPayload {

    String contentType = "content-type";
    Pair<String, String> ctText = Pair.of(contentType, "text/plain");
    Pair<String, String> ctJson = Pair.of(contentType, "application/json");
    String methodPost = "POST";
    String methodGet = "GET";

    default HttpRequest createRequest(
        Function1<HttpRequest.HttpRequestBuilder, HttpRequest.HttpRequestBuilder> builder
    ) {
        return builder.apply(HttpRequest.builder()).build();
    }

    default Url createUrl(Function1<Url.UrlBuilder, Url.UrlBuilder> builder) {
        return builder.apply(Url.builder()).build();
    }

}