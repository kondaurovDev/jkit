package jkit.entry;

import io.vavr.control.Option;
import com.jkit.core.ext.*;
import io.vavr.control.Try;
import lombok.*;

import java.util.HashSet;
import java.util.function.Consumer;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Command {

    CommandDef commandDef;
    AccessChecker accessChecker;
    Executor executor;

    interface Executor {
        Try<?> execute(MethodContext methodContext);
    }

    interface AccessChecker {
        Boolean check(MethodContext ctx);
    }

    private static final HashSet<String> inProgress = new HashSet<>();

    public Try<?> executeBlocking(
        PropMap payload,
        PropMap user
    ) {
        return this
            .commandDef.createContext(payload, user)
            .flatMap(this::executeBlocking);
    }

    public Try<?> executeBlocking(
        MethodContext methodContext
    ) {
       return this.executeBlocking(methodContext, c -> {});
    }

    public Try<?> executeBlocking(
        MethodContext methodContext,
        Consumer<CommandEvent> onSave
    ) {

        val msg = String.format(
            "Execute command: %s, %s",
            commandDef.getName(),
            Option.of(methodContext.getParams()).toString()
        );

        IOExt.log(l -> l.debug(msg));

        if (!accessChecker.check(methodContext))
            return Try.failure(new Error("No rights to execute command"));

        if (!commandDef.getFlag().getParallelRun())
            synchronized (this) {
                if (inProgress.contains(commandDef.getName())) {
                    return Try.failure(new Error("Already in progress"));
                } else {
                    inProgress.add(commandDef.getName());
                }
            }

        val startTime = TimeExt.getCurrent();
        methodContext.log("Execute command: " + commandDef.getName());
        val result = this.executor.execute(methodContext);

        val error = result.fold(
            e -> String.format(
                "Can't execute command (%s): %s",
                commandDef.getName(),
                e.getMessage()
            ),
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
