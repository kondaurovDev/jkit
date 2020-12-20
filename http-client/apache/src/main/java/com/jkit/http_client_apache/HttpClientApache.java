package com.jkit.http_client_apache;

import com.jkit.core.JKitData;
import io.vavr.Function1;
import com.jkit.http.JKitHttpClient;
import com.jkit.http.context.IContext;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;

import com.jkit.core.ext.*;

import lombok.*;

public interface HttpClientApache {

    interface Client extends
        JKitHttpClient<HttpUriRequest, org.apache.http.HttpResponse>,
        IRequestExecutor { }

    static <J> Client create(
        JKitData.IObjMapper<?> objMapper,
        HttpClient httpClient
    ) {
        return HttpClientImpl.create(
            new IContext.Context(objMapper),
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

        IContext context;
        HttpClient httpClient;

    }

}


