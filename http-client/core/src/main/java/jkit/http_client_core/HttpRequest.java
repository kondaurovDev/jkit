package jkit.http_client_core;

import jkit.core.model.Pair;
import jkit.core.model.Url;
import lombok.*;

import java.util.List;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class HttpRequest {

    Url url;
    @Builder.Default
    byte[] entity = null;
    @Builder.Default
    String method = "post";
    @Singular("header")
    List<Pair<String, String>> headers;

}
