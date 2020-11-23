package jkit.core.model;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HttpResponse<A> {

    int code;
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

    public String toString() {
        return List.of(code, codePhrase, body).mkString(":");
    }
}
