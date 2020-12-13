package jkit.http_client_apache;

import io.vavr.Function1;
import jkit.core.JKitData;
import jkit.http_client.JKitHttpClient;
import jkit.http_client.context.IContext;
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
        JKitData.IObjMapperMain<?, ? extends JKitData.IObjMapper<?>> objMapperMain,
        HttpClient httpClient
    ) {
        return HttpClientImpl.create(
            new IContext.Context(objMapperMain),
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


