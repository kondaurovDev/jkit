package jkit.akka_http;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import io.vavr.Function1;
import io.vavr.control.Either;
import jkit.akka_http.route.IPayloadRoute;
import jkit.akka_http.route.ICompleteRoute;
import jkit.core.JKitData;
import jkit.core.ext.IOExt;
import jkit.core.model.UserError;
import lombok.*;

public interface AkkaExt {

    static Either<UserError, AkkaHttpServer> buildAndListen(
        int port,
        String serviceName,
        JKitData.IObjMapperMain<?, ? extends JKitData.IObjMapper<?>> objMapperMain,
        Function1<Router, Route> createRouter
    ) {
        return listenHttp(
            port,
            serviceName,
            d -> createRouter.apply(Router.of(objMapperMain))
        );
    }

    static Either<UserError, AkkaHttpServer> listenHttp(
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

        JKitData.IObjMapperMain<?, ? extends JKitData.IObjMapper<?>> objMapperMain;

        public AllDirectives d() {
            return this;
        }

    }

}
