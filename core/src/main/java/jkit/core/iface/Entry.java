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

    interface IExecuteCmdRequest<U> {
        IRequestPayload getPayload();
        U getUser();
        ResponseType getResponseType();
        DataFormat getResponseFormat();
    }

    interface ICommandMap<U> {
        HashMap<String, ICommand<U>> getMap();
        Either<UserError, ICommand<U>> getCommand(String commandName);
    }

    interface ICommand<U> {
        ICommandDef getCommandDef();
        Entry.AccessChecker<U> getAccessChecker();
        Entry.Executor<U> getExecutor();

        Either<UserError, ?> executeBlocking(IMethodContext<U> methodContext);
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

    interface Executor<U> {
        Either<UserError, ?> execute(IMethodContext<U> methodContext);
    }

    interface AccessChecker<U> {
        Boolean check(IMethodContext<U> ctx);
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

    interface IMethodContext<U> {
        IPropMap getParams();
        U getUser();
        IUserLog getUserLog();
        ArrayList<String> getLogHistory();
    }

    interface IUserLog {
        void end();

        void add(String msg);

        String getLogs();
    }

}
