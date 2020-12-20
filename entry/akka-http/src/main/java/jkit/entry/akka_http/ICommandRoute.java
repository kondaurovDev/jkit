package jkit.entry.akka_http;

import akka.http.javadsl.server.Route;
import io.vavr.Function1;
import com.jkit.akka_http.route.IAuthRoute;
import com.jkit.akka_http.route.IPayloadRoute;
import jkit.core.CorePredef;
import jkit.core.ext.EnumExt;
import jkit.entry.CommandEvent;
import jkit.entry.CommandMap;
import jkit.entry.CommandRequest;
import jkit.entry.ReadyCommand;

import java.util.function.Consumer;

public interface ICommandRoute extends IPayloadRoute, IAuthRoute {

    CommandMap getCommandMap();

    Consumer<CommandEvent> onCommandExecute();

    default Route withCommandName(
        Function1<String, Route> inner
    ) {
        return d().parameter("command", inner);
    }

    default Route withReadyCommand(
        Function1<ReadyCommand, Route> inner
    ) {
        return withCommandRequest(commandRequest ->
            withRight(getCommandMap().getReadyCommand(commandRequest), inner)
        );
    }

    default Route executeCommand() {
        return withReadyCommand(readyCommand ->
            withRight(
                getCommandMap()
                    .getCommand(readyCommand.getCommandDef().getName())
                    .flatMap(cmd -> cmd.executeBlocking(readyCommand.getMethodContext())),
                this::completeJson
            )
        );
    }

    default Route withResponseFormat(
        Function1<CorePredef.DataFormat, Route> inner
    ) {
        return withOptionalParam("responseFormat", formatOpt ->
            formatOpt.fold(
                () -> inner.apply(CorePredef.DataFormat.JSON),
                format -> withRight(
                    EnumExt.getByName(format, CorePredef.DataFormat.class),
                    inner
                )
            )
        );
    }

    default Route withResponseType(
        Function1<CorePredef.ResponseType, Route> inner
    ) {
        return withOptionalParam(
            "responseType", paramOpt -> paramOpt.fold(
                () -> inner.apply(CorePredef.ResponseType.STRICT),
                param -> withRight(EnumExt.getByName(param, CorePredef.ResponseType.class), inner)
            )
        );
    }

    default Route withCommandRequest(
        Function1<CommandRequest, Route> inner
    ) {
        return withUser("auth", user ->
            withCommandName(commandName ->
                withPayload(payload ->
                    inner.apply(CommandRequest.of(
                        commandName,
                        payload,
                        user
                    ))
                )
            )
        );

    }

    default Route commandRoute() {
        return executeCommand();
    }

}