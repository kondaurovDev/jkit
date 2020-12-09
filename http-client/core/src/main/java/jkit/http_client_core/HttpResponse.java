package jkit.http_client_core;

import io.vavr.control.Either;
import jkit.core.ext.IOExt;
import jkit.core.model.UserError;
import lombok.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HttpResponse {

    Integer code;
    String codePhrase;
    ByteArrayOutputStream body;
    Map<String, String> headers;

    Either<UserError, String> getBodyString() {
        return body.toString("UTF-8");
    }

}
