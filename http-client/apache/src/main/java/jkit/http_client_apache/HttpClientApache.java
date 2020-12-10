package jkit.http_client_apache;

import io.vavr.Function1;
import io.vavr.control.Either;
import jkit.core.JKitData;
import jkit.core.model.UserError;
import jkit.http_client_core.HttpResponse;
import jkit.http_client_core.JKitHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;

import jkit.core.ext.*;

import lombok.*;

interface HttpClientApache {

    interface Client extends
        JKitHttpClient.IClient<HttpUriRequest, org.apache.http.HttpResponse>,
        IRequestExecutor
    { }

    static Client create(
        JKitData.IObjMapper<?> objectSerializer,
        HttpClient httpClient
    ) {
        return HttpClientImpl.create(
            objectSerializer,
            httpClient
        );
    }

    static HttpClient createDefaultClient() {
        return HttpClients.createDefault();
    }

    static HttpClient createCustomClient(
        Function1<HttpClientBuilder, HttpClientBuilder> builder
    ) {
        return builder.apply(HttpClientBuilder.create()).build();
    }

    @Value(staticConstructor = "create")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class HttpClientImpl implements Client {

        Logger logger = IOExt.createLogger(HttpClientImpl.class);

        JKitData.IObjMapper<?> jsonObjMapper;
        HttpClient httpClient;

        public Either<UserError, HttpResponse> filterByCode(HttpResponse response, Integer code) {

            if (!response.getCode().equals(code)) {
                return Either.left(UserError.create(response.toString()));
            } else {
                return Either.right(response);
            }

        }

//        public Either<UserError, HttpResponse> uploadFile(
//            String uri,
//            String filePath,
//            Header authHeader
//        ) {
//
//            createPutRequest(uri)
//                .flatMap(r ->
//                    createFileEntity(filePath)
//                        .map(e -> {
//                            r.setEntity(e);
//                            r.setHeader(authHeader);
//                            return r;
//                        })
//                );
//
//
//            return TryExt.get(() -> {
//                createFileEntity(filePath)
//                this.
//                HttpPut req = new HttpPut(uri);
//                req.setEntity(new FileEntity(new File(filePath)));
//                req.setHeader(authHeader);
//                return req;
//            }, "upload file")
//                    .flatMap(e -> getRequestExecutor().apply(e))
//            .flatMap(this::getStringResponse);
//        }

    }

}


