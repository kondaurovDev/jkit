package jkit.akka_http;

import akka.http.javadsl.server.Route;
import io.vavr.Function1;
import io.vavr.control.Either;
import jkit.core.ext.IOExt;
import jkit.core.model.UserError;
import lombok.*;

public interface AkkaExt {

    static Either<UserError, AkkaHttpServer> listenHttp(
        String serviceName,
        Function1<AkkaModule, Route> createRouter,
        int port
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

    static <C extends IServiceConfig> Either<UserError, ?> runServer(
        ServiceFilePath serviceFilePath,
        Function1<AkkaModule, Route> createRoute,
        C serviceConfig
    ) {
        return serviceConfig._getPort()
            .flatMap(port -> AkkaExt.listenHttp(
                serviceFilePath.getServiceName(),
                createRoute,
                port
            ));
    }

}
