package jkit.core.iface;

import io.vavr.collection.HashMap;
import io.vavr.collection.Stream;
import io.vavr.control.Either;
import jkit.core.model.UserError;
import org.reactivestreams.Publisher;

import java.util.Set;
import java.util.function.Consumer;

public interface Entry {

    interface IExecuteCmdRequest {
        String getCommandName();
        IPropMap getPayload();
        IPropMap getUser();
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
        Set<String> getRequiredParams();
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

    interface IUserLog {

        Publisher<String> getPublisher();

        void end();

        void add(String msg);

    }

}
