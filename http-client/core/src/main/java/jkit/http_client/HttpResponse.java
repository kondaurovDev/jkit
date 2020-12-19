package jkit.http_client;

import io.vavr.control.Try;
import jkit.core.ext.StreamExt;
import lombok.*;

import java.io.InputStream;
import java.util.Map;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HttpResponse {

    Integer code;
    String codePhrase;
    InputStream body;
    Map<String, String> headers;

    public Try<String> getBodyString() {
        return StreamExt.inputStreamToString(body);
    }

}
