package jkit.http_client;

import lombok.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HttpResponse {

    Integer code;
    String codePhrase;
    byte[] body;
    Map<String, String> headers;

    public String getBodyString() {
        return new String(body, StandardCharsets.UTF_8);
    }

}
