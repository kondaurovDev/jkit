package jkit.http_client;

import io.vavr.control.Either;
import jkit.core.JKitData;
import jkit.core.model.UserError;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;

import java.io.File;

import jkit.core.ext.*;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HttpClientImpl implements IEntity, IReadResponse, IFactory {

    Logger logger = IOExt.createLogger(HttpClientImpl.class);

    JKitData.IObjMapper<?> jsonObjMapper;
    HttpClient httpClient;

    public static HttpClientImpl createDefault(
        JKitData.IObjMapper<?> objectSerializer
    ) {
        return HttpClientImpl.create(
            objectSerializer,
            HttpClients.createDefault()
        );
    }

    static <A extends HttpUriRequest> A setBasicAuth(
        A request,
        String user,
        String password
    ) {
        request.setHeader(
            HttpHeader.basicAuth(
                user,
                password
            )
        );
        return request;
    }

    public <A> Either<UserError, HttpResponse<A>> filterByCode(HttpResponse<A> response, Integer code) {

        if (!response.getCode().equals(code)) {
            return Either.left(UserError.create(response.toString()));
        } else {
            return Either.right(response);
        }

    }

    public Either<UserError, HttpResponse<String>> uploadFile(
        String uri,
        String filePath,
        Header authHeader
    ) {
        return TryExt.get(() -> {
            HttpPut req = new HttpPut(uri);
            req.setEntity(new FileEntity(new File(filePath)));
            req.setHeader(authHeader);
            return req;
        }, "upload file")
        .flatMap(this::getStringResponse);
    }

}
