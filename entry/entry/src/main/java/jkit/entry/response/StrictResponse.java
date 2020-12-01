package jkit.entry.api.response;

import jkit.core.iface.Entry;
import jkit.entry.CommandEvent;
import jkit.entry.IApi;
import jkit.entry.Command;
import jkit.entry.MethodContext;
import lombok.*;

import java.util.function.Consumer;
import java.util.stream.Stream;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class StrictResponse<U> implements IApi.IResponse {

    Command<U> command;
    MethodContext<U> methodContext;
    Entry.DataFormat responseFormat;

    public Stream<?> getResponse(
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