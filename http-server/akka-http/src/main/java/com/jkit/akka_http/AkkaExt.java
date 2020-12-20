package com.jkit.akka_http;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import com.jkit.core.JKitData;
import io.vavr.Function1;
import io.vavr.control.Try;
import com.jkit.akka_http.route.IPayloadRoute;
import com.jkit.akka_http.route.ICompleteRoute;
import com.jkit.core.ext.IOExt;
import lombok.*;

public interface AkkaExt {

    interface CreateRoute {
        Route createRoute(Router router);
    }

    static Try<AkkaHttpServer> buildAndListen(
        int port,
        String serviceName,
        JKitData.IObjMapper<?> jackson,
        CreateRoute createRouter
    ) {
        return listenHttp(
            port,
            serviceName,
            d -> createRouter.createRoute(Router.of(jackson))
        );
    }

    static Try<AkkaHttpServer> listenHttp(
        int port,
        String serviceName,
        Function1<AkkaModule, Route> createRouter
    ) {
        val akkaModule = AkkaModule.create(serviceName);

        val router = createRouter.apply(
            akkaModule
        );

        val akkaHttpServer = AkkaHttpServer.create(
            akkaModule,
            router,
            port
        );

        return akkaHttpServer.bind().map(r -> {
            IOExt.out(String.format("%s service is listening http on %s", serviceName, r));
            return akkaHttpServer;
        });
    }

    @Value(staticConstructor = "of")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
    class Router extends AllDirectives
        implements
            ICompleteRoute,
            IPayloadRoute
    {

        JKitData.IObjMapper<?> objMapper;

        public AllDirectives d() {
            return this;
        }

    }

}
