package jkit.entry;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Either;
import jkit.core.ext.ListExt;
import jkit.core.iface.Entry;
import jkit.core.model.UserError;
import lombok.*;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder @Value
public class CommandDef implements Entry.ICommandDef {

    @EqualsAndHashCode.Include
    String name;
    @Builder.Default
    CommandFlag flag = CommandFlag.simpleTask;
    @Singular
    List<Entry.IPropDef<?>> params;

    public static CommandDef of(String name) {
        return new CommandDef(
            name,
            CommandFlag.simpleTask,
            Collections.emptyList()
        );
    }

    public <U> Command<U> register(
        CommandMap<U> commandMap,
        Entry.AccessChecker<U> accessChecker,
        Entry.Executor<U> executor
    ) {
        val cmd = Command.of(
            this,
            accessChecker,
            executor
        );
        commandMap.register(cmd);
        return cmd;
    }

    public Either<UserError, PropMap> processParams(
        Map<String, Object> map
    ) {
        return ListExt.applyToEach(
            params,
            p -> map.get(p.getName()).fold(
                () -> Either.left(UserError.create(String.format("Missing property '%s' ", p.getName()))),
                o -> p.processInput(o).map(v -> Tuple.of(p.getName(), v))
            ),
            "validate",
            true
        ).map(lst -> PropMap.create(HashMap.ofEntries(lst)));
    }

    public ReadyCommand createReadyCommand(Map<String, Object> params) {
        return ReadyCommand.of(PropMap.create(params), getName());
    }
//    public Either<UserError, ReadyCommand> createReadyCommandFromObject(Object params) {
//        return ReadyCommand.fromObject(getName(), params);
//    }

}