package com.jkit.akka_http.route;

import akka.http.javadsl.server.Route;
import com.jkit.core.JKitEntry;
import io.vavr.Function1;
import java.util.function.Consumer;

public interface ICommandRoute extends IPayloadRoute, IAuthRoute {

    JKitEntry.ICommandMap getCommandMap();

    Consumer<JKitEntry.ICommandEvent> onCommandExecute();

    JKitEntry.CommandRequestBuilder requestBuilder();

    default Route withCommandName(
        Function1<String, Route> inner
    ) {
        return d().parameter("command", inner);
    }

    default Route withReadyCommand(
        Function1<JKitEntry.IReadyCommand, Route> inner
    ) {
        return withCommandRequest(commandRequest ->
            withSuccess(getCommandMap().getReadyCommand(commandRequest), inner)
        );
    }

    default Route executeCommand() {
        return withReadyCommand(readyCommand ->
            withSuccess(
                getCommandMap()
                    .getCommand(readyCommand.getCommandDef().getName())
                    .flatMap(cmd -> cmd.executeBlocking(readyCommand.getMethodContext())),
                this::completeJson
            )
        );
    }

//    default Route withResponseType(
//        Function1<CorePredef.ResponseType, Route> inner
//    ) {
//        return withOptionalParam(
//            "responseType", paramOpt -> paramOpt.fold(
//                () -> inner.apply(CorePredef.ResponseType.STRICT),
//                param -> withRight(EnumExt.getByName(param, CorePredef.ResponseType.class), inner)
//            )
//        );
//    }

    default Route withCommandRequest(
        Function1<JKitEntry.ICommandRequest, Route> inner
    ) {
        return withUser("auth", user ->
            withCommandName(commandName ->
                withPayload(payload ->
                    inner.apply(requestBuilder().build(
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