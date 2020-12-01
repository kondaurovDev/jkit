package jkit.akka_http.route;

import akka.http.javadsl.server.Route;
import io.vavr.*;
import io.vavr.collection.HashMap;
import jkit.core.ext.EnumExt;
import jkit.akka_http.AkkaModule;
import jkit.core.iface.Entry;

import java.util.function.Consumer;

public interface ICommandRoute<U> extends IPayloadRoute, IAuthRoute<U> {

    AkkaModule getAkkaModule();

    Function1<Router.Full<U>, Route> getRoute();

    Entry.ICommandMap<U> getCommandMap();

    Consumer<Entry.ICommandEvent> onCommandExecute();

    default Route withCommand(
        Function1<Entry.ICommand<U>, Route> inner
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
        Function1<Entry.DataFormat, Route> inner
    ) {
        return withOptionalParam("responseFormat", formatOpt ->
            formatOpt.fold(
                () -> inner.apply(Entry.DataFormat.JSON),
                format -> withRight(
                    EnumExt.getByName(format, Entry.DataFormat.class),
                    inner
                )
            )
        );
    }

    default Route withResponseType(
        Function1<Entry.ResponseType, Route> inner
    ) {
        return withOptionalParam(
            "responseType", paramOpt -> paramOpt.fold(
                () -> inner.apply(Entry.ResponseType.STRICT),
                param -> withRight(EnumExt.getByName(param, Entry.ResponseType.class), inner)
            )
        );
    }


    default Route withRequest(
        Function1<Entry.IExecuteCmdRequest<U>, Route> inner,
        Function4<HashMap<String, Object>, U, Entry.ResponseType, Entry.DataFormat, Entry.IExecuteCmdRequest<U>> build
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