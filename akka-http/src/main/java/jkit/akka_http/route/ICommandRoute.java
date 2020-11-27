//package jkit.akka_http.route;
//
//import akka.http.javadsl.server.Route;
//import io.vavr.Function1;
//import jkit.core.ext.EnumExt;
//import jkit.akka_http.AkkaModule;
//import jc.server.api.CommandMap;
//import jc.server.api.IApi;
//import jc.server.model.CommandEvent;
//import jc.server.api.Command;
//import jc.server.model.ExecuteCmdRequest;
//
//import java.util.function.Consumer;
//
//public interface ICommandRoute<U> extends IPayloadRoute, IAuthRoute<U> {
//
//    AkkaModule getAkkaModule();
//
//    Function1<Router.Full<U>, Route> getRoute();
//
//    CommandMap<U> getCommandMap();
//
//    Consumer<CommandEvent> onCommandExecute();
//
//    default Route withCommand(
//        Function1<Command<U>, Route> inner
//    ) {
//        return d().parameter("command", commandName  ->
//            withRight(
//                getCommandMap().getCommand(commandName),
//                inner
//            )
//        );
//    }
//
//    default Route executeCommand() {
//        return withCommand(cmd ->
//            withRequest(request ->
//                withRight(cmd.createResponse(
//                    request,
//                    getAkkaModule()
//                ), r -> r.getRoute(this, onCommandExecute()))
//            )
//        );
//    }
//
//    default Route withResponseFormat(
//        Function1<IApi.DataFormat, Route> inner
//    ) {
//        return withOptionalParam("responseFormat", formatOpt ->
//            formatOpt.fold(
//                () -> inner.apply(IApi.DataFormat.json),
//                format -> withRight(
//                    EnumExt.getByName(format, IApi.DataFormat.class),
//                    inner
//                )
//            )
//        );
//    }
//
//    default Route withResponseType(
//        Function1<IApi.ResponseType, Route> inner
//    ) {
//        return withOptionalParam(
//            "responseType", paramOpt -> paramOpt.fold(
//                () -> inner.apply(IApi.ResponseType.strict),
//                param -> withRight(IApi.ResponseType.fromString(param), inner)
//            )
//        );
//    }
//
//
//    default Route withRequest(
//        Function1<ExecuteCmdRequest<U>, Route> inner
//    ) {
//        return withUser("auth", user ->
//            withCommand(command ->
//                withPayload(payload ->
//                    withResponseType(responseType ->
//                        withResponseFormat(responseFormat ->
//                            inner.apply(ExecuteCmdRequest.of(
//                                payload,
//                                user,
//                                responseType,
//                                responseFormat
//                            ))
//                        )
//                    )
//                )
//            )
//        );
//
//    }
//
//    default Route commandRoute() {
//        return executeCommand();
//    }
//
//}