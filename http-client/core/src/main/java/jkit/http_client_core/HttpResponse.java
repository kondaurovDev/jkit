package jkit.http_client_core;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.*;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HttpResponse<A> {

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
