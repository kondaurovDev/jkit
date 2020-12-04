package jkit.core;

import io.vavr.collection.Stream;
import io.vavr.control.Either;
import jkit.core.model.UserError;
import org.reactivestreams.Publisher;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface JKitEntry {

    interface ICommandRequest {
        String getCommandName();
        IPropMap getPayload();
        IPropMap getUser();
    }

    interface ICommandMap {
        Map<String, ICommand> getMap();
        Either<UserError, ICommand> getCommand(String commandName);
        Either<UserError, JKitEntry.IReadyCommand> getReadyCommand(
            JKitEntry.ICommandRequest commandRequest
        );
    }

    interface ICommand {
        ICommandDef getCommandDef();
        JKitEntry.AccessChecker getAccessChecker();
        JKitEntry.Executor getExecutor();

        Either<UserError, ?> executeBlocking(IMethodContext methodContext);
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
        Either<UserError, JKitEntry.IMethodContext> createContext(
            IPropMap payload,
            IPropMap user
        );
        Either<UserError, IReadyCommand> createReadyCommand(
            IPropMap payload,
            IPropMap user
        );
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

    interface IPropDef<A> {
        String getName();
        Class<A> getParamClass();
        Boolean getIsList();

        Either<UserError, ?> validateObj(Object obj);
    }

    interface IPropMap {
        Map<String, Object> getParams();
        <A> Either<UserError, A> propOpt(IPropDef<A> prop);
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

        Publisher<String> getPublisher();

        void end();

        void add(String msg);

    }

}
