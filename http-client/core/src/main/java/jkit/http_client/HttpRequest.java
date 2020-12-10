package jkit.http_client;

import io.vavr.CheckedFunction0;
import io.vavr.Tuple2;
import jkit.core.model.Pair;
import jkit.core.model.Url;
import jkit.http_client.context.IResponse;
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
