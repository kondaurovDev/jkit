package jkit.entry;

import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.headers.HttpCookie;
import akka.http.javadsl.server.Route;
import io.vavr.control.Option;
import jkit.entry.route.IRouter;
import jkit.entry.api.IApi;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Consumer;

@Setter @Accessors(fluent = true)
public class ResponseHolder implements IApi.IResponse {

    HttpResponse response;
    HttpCookie cookie;

    public Route getRoute(
        IRouter router,
        Consumer<CommandEvent> onSave
    ) {
        return router.d().setCookie(Option.of(cookie), () ->
            Option.of(response).fold(
                () -> router.d().complete("empty result"),
                r -> router.d().complete(r)
            )
        );
    }

}