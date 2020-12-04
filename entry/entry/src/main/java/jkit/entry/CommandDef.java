package jkit.entry;

import io.vavr.Tuple;
import io.vavr.control.Either;
import jkit.core.ext.ListExt;
import jkit.core.ext.MapExt;
import jkit.core.iface.Entry;
import jkit.core.model.UserError;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder @Value
public class CommandDef implements Entry.ICommandDef {

    @EqualsAndHashCode.Include
    String name;
    @Builder.Default
    CommandFlag flag = CommandFlag.simpleTask;
    @Singular
    List<Entry.IPropDef<?>> params;
    @Singular(value = "required")
    Set<String> requiredParams;

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
        Entry.AccessChecker accessChecker,
        Entry.Executor executor
    ) {
        val cmd = Command.of(
            this,
            accessChecker,
            executor
        );
        commandMap.register(cmd);
        return cmd;
    }

    public Either<UserError, PropMap> parseMap(
        PropMap propMap
    ) {
        return ListExt.applyToEach(
            params,
            p -> MapExt.get(p.getName(), propMap.getParams(), "Missing prop").fold(
                err -> {
                    if (requiredParams.contains(p.getName()))
                        return Either.left(UserError.create(String.format("Missing property '%s' ", p.getName())));
                    return Either.right(null);
                },
                o -> p.validateObj(o).map(v -> Tuple.of(p.getName(), v))
            ),
            "validate",
            true
        ).map(lst -> PropMap.create().params(lst.toJavaMap(t -> t)).build());
    }

    public Either<UserError, ReadyCommand> createReadyCommand(
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
            )
            .map(ctx -> ReadyCommand.of(this, ctx));
    }

}