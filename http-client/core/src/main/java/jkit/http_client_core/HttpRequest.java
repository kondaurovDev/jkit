package jkit.http_client_core;

import lombok.*;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class HttpRequest {

    String url;
    @Builder.Default
    ByteArrayOutputStream entity = null;
    @Builder.Default
    String method = "post";
    @Singular("param")
    Map<String, String> queryParams;
    @Singular("header")
    Map<String, String> headers;

}
