package jkit.http_client_apache;

import io.vavr.Function1;
import jkit.core.JKitData;
import jkit.http_client.JKitHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;

import jkit.core.ext.*;

import lombok.*;

public interface HttpClientApache {

    interface Client extends
        JKitHttpClient<HttpUriRequest, org.apache.http.HttpResponse>,
        IRequestExecutor { }

    static <J> Client create(
        JKitData.IObjMapper<J> objectSerializer,
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

    }

}


