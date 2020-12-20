package com.jkit.http.context;

import com.jkit.http.Url;
import io.vavr.Function1;
import com.jkit.core.JKitData;
import com.jkit.core.model.Pair;
import lombok.*;

public interface IContext extends IHeader, IPayload, IResponse {

    String contentType = "Content-Type";
    String contentLength = "Content-Length";
    Pair<String, String> ctText = Pair.of(contentType, "text/plain");
    Pair<String, String> ctJson = Pair.of(contentType, "application/json");
    String methodPost = "POST";
    String methodGet = "GET";

    default Url createUrl(Function1<Url.UrlBuilder, Url.UrlBuilder> builder) {
        return builder.apply(Url.builder()).build();
    }

    @Value
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class Context implements IContext {
        JKitData.IObjMapper<?> objMapper;
    }

}
