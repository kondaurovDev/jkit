package jkit.entry;

import io.vavr.Tuple;
import io.vavr.control.Try;
import com.jkit.core.ext.ListExt;
import com.jkit.core.ext.*;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder @Value
public class CommandDef {

    @EqualsAndHashCode.Include
    String name;
    @Builder.Default
    CommandFlag flag = CommandFlag.simpleTask;
    @Singular
    List<PropDef<?>> props;
    @Singular(value = "required")
    Set<String> requiredProps;

    public static CommandDef of(String name) {
        return new CommandDef(
            name,
            CommandFlag.simpleTask,
            Collections.emptyList(),
            Collections.emptySet()
        );
    }

    public Command register(
        CommandMap commandMap,
        Command.AccessChecker accessChecker,
        Command.Executor executor
    ) {
        val cmd = Command.of(
            this,
            accessChecker,
            executor
        );
        commandMap.register(cmd);
        return cmd;
    }

    public Try<PropMap> parseMap(
        PropMap propMap
    ) {
        return ListExt.applyToEach(
            props,
            p -> MapExt.get(p.getName(), propMap.getProps(), "Missing prop").fold(
                err -> {
                    if (requiredProps.contains(p.getName()))
                        return Try.failure(new Error(String.format("Missing property '%s' ", p.getName())));
                    return Try.success(null);
                },
                o -> p.validateObj(o).map(v -> Tuple.of(p.getName(), v))
            ),
            "validate",
            true
        ).map(lst -> PropMap.create().props(lst.toJavaMap(t -> t)).build());
    }

    public Try<MethodContext> createContext(
        PropMap payload,
        PropMap user
    ) {
        return this
            .parseMap(payload)
            .map(payloadProcessed ->
                MethodContext.of(
                    payloadProcessed,
                    user,
                    UserLog.create()
                )
            );
    }

    public Try<ReadyCommand> createReadyCommand(
        PropMap payload,
        PropMap user
    ) {
        return this
            .createContext(
                payload,
                user
            )
            .map(ctx -> ReadyCommand.of(this, ctx));
    }

}