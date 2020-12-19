package jkit.akka_http;

import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.Route;
import io.vavr.control.Try;
import jkit.core.ext.*;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value
public class AkkaHttpServer {

    AkkaModule akkaModule;
    Http http;
    Route router;
    int port;

    public static AkkaHttpServer create(
        AkkaModule akkaModule,
        Route route,
        int port
    ) {
        return new AkkaHttpServer(
            akkaModule,
            Http.get(akkaModule.getActorSystem()),
            route,
            port
        );
    }

    public Try<String> bind()  {

        val routeFlow = router
            .flow(akkaModule.getActorSystem(), akkaModule.getMaterializer());

        val connectTo = ConnectHttp.toHost("0.0.0.0", port);

        val binding = TryExt.await(
            http.bindAndHandle(
                routeFlow,
                connectTo,
                akkaModule.getMaterializer()
            ).toCompletableFuture()
        ).map(r -> r.localAddress().toString());

        val msg = binding.fold(
            err -> "Can't start server: " + err.toString(),
            res -> "Server online at " + res
        );

        IOExt.out(msg);

        return binding;

    }

}