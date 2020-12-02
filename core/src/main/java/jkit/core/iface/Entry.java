package jkit.core.iface;

import io.vavr.collection.HashMap;
import io.vavr.collection.Stream;
import io.vavr.control.Either;
import jkit.core.model.UserError;

import java.util.ArrayList;
import java.util.function.Consumer;

public interface Entry {

    enum DataFormat {
        JSON,
        YAML;
    }

    enum ResponseType {
        STREAM,
        STRICT;
    }

    interface IRequestPayload {
        String getValue();
        DataFormat getDataFormat();

        Either<UserError, HashMap<String, Object>> getMap();
    }

    interface IExecuteCmdRequest {
        IPropMap getPayload();
        IPropMap getUser();
        ResponseType getResponseType();
    }

    interface ICommandMap {
        HashMap<String, ICommand> getMap();
        Either<UserError, ICommand> getCommand(String commandName);
    }

    interface ICommand {
        ICommandDef getCommandDef();
        Entry.AccessChecker getAccessChecker();
        Entry.Executor getExecutor();

        Either<UserError, ?> executeBlocking(IMethodContext methodContext);
    }

    interface ICommandFlag {
        Boolean getSaveEvent();
        Boolean getParallelRun();
    }

    interface ICommandDef {
        String getName();
        ICommandFlag getFlag();
        java.util.List<IPropDef<?>> getParams();
    }

    interface ICommandResult {

        default Stream<?> getResponse() {
           return this.getResponse(e -> {});
        }

        Stream<?> getResponse(
            Consumer<ICommandEvent> onSave
        );

    }

    interface ICommandEvent {

    }

    interface Executor {
        Either<UserError, ?> execute(IMethodContext methodContext);
    }

    interface AccessChecker {
        Boolean check(IMethodContext ctx);
    }

    interface Name {
        String getName();
    }

    interface IPropDef<A> {
        String getName();
        Class<A> getParamClass();
        Boolean getIsList();

        Either<UserError, ?> processInput(Object obj);
    }

    interface IPropMap {
        <A> Either<UserError, A> paramValueOpt(IPropDef<A> param);
    }

    interface IMethodContext {
        IPropMap getParams();
        IPropMap getUser();
        IUserLog getUserLog();
        ArrayList<String> getLogHistory();
    }

    interface IUserLog {
        void end();

        void add(String msg);

        String getLogs();
    }

}
