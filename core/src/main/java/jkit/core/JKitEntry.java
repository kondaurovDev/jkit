//package jkit.core;
//
//import io.vavr.control.Either;
//import io.vavr.control.Try;
//import jkit.core.model.JKitError;
//import org.reactivestreams.Publisher;
//
//import java.util.Map;
//import java.util.Set;
//
//public interface JKitEntry {
//
//    interface ICommandRequest {
//        String getCommandName();
//        Map<String, Object> getPayload();
//        Map<String, Object> getUser();
//    }
//
//    interface ICommandMap {
//        Map<String, ICommand> getMap();
//        Either<JKitError, ICommand> getCommand(String commandName);
//        Either<JKitError, JKitEntry.IReadyCommand> getReadyCommand(
//            JKitEntry.ICommandRequest commandRequest
//        );
//    }
//
//    interface ICommand {
//        ICommandDef getCommandDef();
//        JKitEntry.AccessChecker getAccessChecker();
//        JKitEntry.Executor getExecutor();
//
//        Either<JKitError, ?> executeBlocking(IMethodContext methodContext);
//    }
//
//    interface ICommandFlag {
//        Boolean getSaveEvent();
//        Boolean getParallelRun();
//    }
//
//    interface ICommandDef {
//        String getName();
//        ICommandFlag getFlag();
//        java.util.List<IPropDef<?>> getProps();
//        Set<String> getRequiredProps();
//        Either<JKitError, JKitEntry.IMethodContext> createContext(
//            IPropMap payload,
//            IPropMap user
//        );
//        Either<JKitError, IReadyCommand> createReadyCommand(
//            IPropMap payload,
//            IPropMap user
//        );
//    }
//
//    interface ICommandEvent {
//
//    }
//
//    interface Executor {
//        Either<JKitError, ?> execute(IMethodContext methodContext);
//    }
//
//    interface AccessChecker {
//        Boolean check(IMethodContext ctx);
//    }
//
//    interface IPropDef<A> {
//        String getName();
//        Class<A> getParamClass();
//        Boolean getIsList();
//        Try<Object> validateObj(Object obj);
//    }
//
//    interface IPropMap {
//        Map<String, Object> getProps();
//        <A> Either<JKitError, A> propOpt(IPropDef<A> prop);
//        <A> A prop(IPropDef<A> prop);
//    }
//
//    interface IMethodContext {
//        IPropMap getParams();
//        IPropMap getUser();
//        IUserLog getUserLog();
//
//        default void log(String msg) {
//            this.getUserLog().add(msg);
//        }
//    }
//
//    interface IReadyCommand {
//        JKitEntry.ICommandDef getCommandDef();
//        JKitEntry.IMethodContext getMethodContext();
//    }
//
//    interface IUserLog {
//
//        Publisher<String> getPublisher();
//
//        void end();
//
//        void add(String msg);
//
//    }
//
//}
