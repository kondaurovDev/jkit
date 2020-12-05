package jkit.http_client;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import jkit.core.JKitHttpClient;
import lombok.*;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HttpResponse<A> implements JKitHttpClient.IHttpResponse<A> {

    Integer code;
    String codePhrase;
    A body;
    List<Tuple2<String, String>> headers;

    public <B> HttpResponse<B> copy(B newBody) {
        return HttpResponse.create(
            code,
            codePhrase,
            newBody,
            headers
        );
    }

}
