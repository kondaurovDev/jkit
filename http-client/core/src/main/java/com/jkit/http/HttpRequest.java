package com.jkit.http;

import com.jkit.http.context.IResponse;
import io.vavr.CheckedFunction0;
import com.jkit.core.model.Pair;
import lombok.*;

import java.util.List;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class HttpRequest {

    Url url;
    @Builder.Default
    CheckedFunction0<Payload> entity = null;
    @Builder.Default
    String method = null;
    @Singular("header")
    List<Pair<String, String>> headers;
    @Builder.Default
    IResponse.CheckCode checkResponse = IResponse.response200;

}
