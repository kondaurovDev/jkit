package jkit.entry;

import io.vavr.control.Either;
import io.vavr.control.Option;
import com.jkit.core.ext.*;
import lombok.*;

import java.util.HashSet;
import java.util.function.Consumer;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Command {

    CommandDef commandDef;
    JKitEntry.AccessChecker accessChecker;
    JKitEntry.Executor executor;

    private static final HashSet<String> inProgress = new HashSet<>();

    public Either<JKitError, ?> executeBlocking(
        PropMap payload,
        PropMap user
    ) {
        return this
            .commandDef.createContext(payload, user)
            .flatMap(this::executeBlocking);
    }

    public Either<JKitError, ?> executeBlocking(
        JKitEntry.IMethodContext methodContext
    ) {
       return this.executeBlocking(methodContext, c -> {});
    }

    public Either<JKitError, ?> executeBlocking(
        JKitEntry.IMethodContext methodContext,
        Consumer<CommandEvent> onSave
    ) {

        val msg = String.format(
            "Execute command: %s, %s",
            commandDef.getName(),
            Option.of(methodContext.getParams()).toString()
        );

        IOExt.log(l -> l.debug(msg));

        if (!accessChecker.check(methodContext))
            return Either.left(JKitError.create("No rights to execute command"));

        if (!commandDef.getFlag().getParallelRun())
            synchronized (this) {
                if (inProgress.contains(commandDef.getName())) {
                    return Either.left(JKitError.create("Already in progress"));
                } else {
                    inProgress.add(commandDef.getName());
                }
            }

        val startTime = TimeExt.getCurrent();
        methodContext.log("Execute command: " + commandDef.getName());
        val result = this.executor.execute(methodContext)
            .mapLeft(e -> e.withError("Can't execute: " + commandDef.getName()));

        val error = result.fold(
            JKitError::toString,
            res -> null
        );

        if (!commandDef.getFlag().getParallelRun()) {
            inProgress.remove(commandDef.getName());
        }

        if (this.commandDef.getFlag().getSaveEvent()) {
            val ev = CommandEvent.of(
                TimeExt.getTimestamp(startTime.toDateTime()),
                TimeExt.getTimestamp(TimeExt.getCurrent().toDateTime()),
                commandDef.getName(),
                "",
                error
            );
            onSave.accept(ev);
        }

        return result;
    }

}
