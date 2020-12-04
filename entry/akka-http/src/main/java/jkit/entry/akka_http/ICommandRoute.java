package jkit.entry.akka_http;

import akka.http.javadsl.server.Route;
import io.vavr.Function1;
import jkit.akka_http.route.IAuthRoute;
import jkit.akka_http.route.IPayloadRoute;
import jkit.core.CorePredef;
import jkit.core.ext.EnumExt;
import jkit.core.JKitEntry;
import jkit.entry.CommandRequest;
import jkit.entry.PropMap;

import java.util.function.Consumer;

public interface ICommandRoute extends IPayloadRoute, IAuthRoute {

    abstract class Router implements ICommandRoute {}

    JKitEntry.ICommandMap getCommandMap();

    Consumer<JKitEntry.ICommandEvent> onCommandExecute();

    default Route withCommandName(
        Function1<String, Route> inner
    ) {
        return d().parameter("command", inner);
    }

    default Route withReadyCommand(
        Function1<JKitEntry.IReadyCommand, Route> inner
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
                        PropMap.create().params(payload).build(),
                        PropMap.create().params(user).build()
                    ))
                )
            )
        );

    }

    default Route commandRoute() {
        return executeCommand();
    }

}