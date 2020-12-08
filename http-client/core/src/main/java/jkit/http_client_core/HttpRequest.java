package jkit.http_client_core;

import lombok.*;

import java.util.Map;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class HttpRequest {

    String url;
    String entity;
    @Singular("param")
    Map<String, String> queryParams;
    @Singular("header")
    Map<String, String> headers;

}
