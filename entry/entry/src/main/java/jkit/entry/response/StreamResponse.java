package jkit.entry.response;

import akka.http.javadsl.server.Route;
import io.vavr.Function0;
import io.vavr.concurrent.Future;
import jkit.entry.CommandEvent;
import jkit.entry.route.IRouter;
import jkit.entry.Command;
import jkit.entry.api.IApi;
import jkit.core.ext.IOExt;
import jkit.entry.MethodContext;
import lombok.*;

import java.util.function.Consumer;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class StreamResponse<U> implements IApi.IResponse {

    Command<U> command;
    MethodContext<U> methodContext;

    public <A> A value(Function0<A> create) {
        return create.apply();
    }

    public Route getRoute(
        IRouter router,
        Consumer<CommandEvent> onSave
    ) {
        val s = methodContext.getLog().getSseSource();

        Future.of(() -> command
            .execute(methodContext, onSave)
            .map(r -> {
                methodContext.log("successful done");
                return r;
            }).mapLeft(e -> {
                methodContext.log("done with error: " + e.toString());
                IOExt.log(l -> l.warn(e.toString()));
                return e;
            }))
        .onComplete(r -> {
            IOExt.log(l -> l.debug("Completing request: " + command.getCommandDef().getName()));
            methodContext.getLog().end();
        });

        return router.completeSse(s);
    }
}