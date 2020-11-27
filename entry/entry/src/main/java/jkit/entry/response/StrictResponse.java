package jkit.entry.api.response;

import akka.http.javadsl.server.Route;
import jkit.entry.CommandEvent;
import jkit.entry.route.IRouter;
import jkit.entry.Command;
import jkit.entry.api.IApi;
import jkit.entry.api.IUserResponse;
import jkit.entry.MethodContext;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.function.Consumer;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class StrictResponse<U> implements IApi.IResponse {

    Command<U> command;
    MethodContext<U> methodContext;
    IApi.DataFormat responseFormat;

    public Route getResponse(
        Consumer<CommandEvent> onSave
    ) {
        return router.withRight(
            command.execute(methodContext, onSave)
                .map(obj -> switch (responseFormat) {
                    case yaml: yield IUserResponse.yaml(obj);
                    case json: yield IUserResponse.json(obj);
                }),
            r -> r.getRoute(router)
        );
    }

}