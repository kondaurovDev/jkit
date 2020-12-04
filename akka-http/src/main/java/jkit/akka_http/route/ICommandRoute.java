package jkit.akka_http.route;

import akka.http.javadsl.server.Route;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.HashMap;
import jkit.core.CorePredef;
import jkit.core.ext.EnumExt;
import jkit.akka_http.AkkaModule;
import jkit.core.JKitEntry;

import java.util.function.Consumer;

public interface ICommandRoute extends IPayloadRoute, IAuthRoute {

    AkkaModule getAkkaModule();

    Function1<Router.Full, Route> getRoute();

    JKitEntry.ICommandMap getCommandMap();

    Consumer<JKitEntry.ICommandEvent> onCommandExecute();

    default Route withCommand(
        Function1<JKitEntry.ICommand, Route> inner
    ) {
        return d().parameter("command", commandName  ->
            withRight(
                getCommandMap().getCommand(commandName),
                inner
            )
        );
    }

    default Route executeCommand() {
        return withCommand(cmd ->
            withRequest(request ->
                withRight(cmd.createResponse(
                    request,
                    getAkkaModule()
                ), r -> r.getRoute(this, onCommandExecute())
                )
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


    default Route withRequest(
        Function1<JKitEntry.IExecuteCmdRequest, Route> inner,
        Function2<JKitEntry.IPropMap, JKitEntry.IExecuteCmdRequest> build
    ) {
        return withUser("auth", user ->
            withCommand(command ->
                withPayload(payload ->
                    withResponseType(responseType ->
                        withResponseFormat(responseFormat ->
                            inner.apply(build.apply(
                                payload,
                                user,
                                responseType,
                                responseFormat
                            ))
                        )
                    )
                )
            )
        );

    }

    default Route commandRoute() {
        return executeCommand();
    }

}