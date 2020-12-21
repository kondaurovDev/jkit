package com.jkit.core;

import io.vavr.collection.List;
import io.vavr.control.Try;

import java.util.Map;
import java.util.Set;

public interface JKitEntry {

    interface CommandRequestBuilder {

        ICommandRequest build(
            String commandName,
            Map<String, ?> payload,
            Map<String, ?> user
        );

    }

    interface ICommandRequest {
        String getCommandName();
        Map<String, ?> getPayload();
        Map<String, ?> getUser();
    }

    interface ICommandMap {
        Map<String, ICommand> getMap();
        Try<ICommand> getCommand(String commandName);
        Try<IReadyCommand> getReadyCommand(
            JKitEntry.ICommandRequest commandRequest
        );
    }

    interface ICommand {
        ICommandDef getCommandDef();
        JKitEntry.AccessChecker getAccessChecker();
        JKitEntry.Executor getExecutor();

        Try<?> executeBlocking(IMethodContext methodContext);
    }

    interface ICommandFlag {
        Boolean getSaveEvent();
        Boolean getParallelRun();
    }

    interface ICommandDef {
        String getName();
        ICommandFlag getFlag();
        java.util.List<IPropDef<?>> getProps();
        Set<String> getRequiredProps();
        Try<JKitEntry.IMethodContext> createContext(
            IPropMap payload,
            IPropMap user
        );
        Try<IReadyCommand> createReadyCommand(
            IPropMap payload,
            IPropMap user
        );
    }

    interface ICommandEvent {

    }

    interface Executor {
        Try<?> execute(IMethodContext methodContext);
    }

    interface AccessChecker {
        Boolean check(IMethodContext ctx);
    }

    interface IPropDef<A> {
        String getName();
        Class<A> getParamClass();
        Boolean getIsList();
        Try<A> validateObj(Object obj);
        Try<List<A>> validateList(Object object);
    }

    interface IPropMap {
        Map<String, Object> getProps();
        <A> Try<A> propOpt(IPropDef<A> prop);
        <A> A prop(IPropDef<A> prop);
    }

    interface IMethodContext {
        IPropMap getParams();
        IPropMap getUser();
        IUserLog getUserLog();

        default void log(String msg) {
            this.getUserLog().add(msg);
        }
    }

    interface IReadyCommand {
        JKitEntry.ICommandDef getCommandDef();
        JKitEntry.IMethodContext getMethodContext();
    }

    interface IUserLog {

//        Publisher<String> getPublisher();

        void end();

        void add(String msg);

    }

}
