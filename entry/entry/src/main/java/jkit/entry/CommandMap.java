package jkit.entry;

import io.vavr.control.Either;
import jkit.core.iface.Entry;
import jkit.core.model.UserError;
import lombok.*;

import java.util.HashMap;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommandMap {

    HashMap<String, Command> map;

    public static CommandMap create() {
        return CommandMap.of(
            new HashMap<>()
        );
    }

    public Command create(
        CommandDef commandDef,
        Entry.AccessChecker accessChecker,
        Entry.Executor executor
    ) {
        val cmd = Command.of(
            commandDef,
            accessChecker,
            executor
        );
        this.register(cmd);
        return cmd;
    }

    public void register(Command command) {
        if (map.containsKey(command.getCommandDef().getName())) {
            throw new Error("Command '" + command.getCommandDef().getName() + "' has been registered already");
        } else {
            map.put(command.getCommandDef().getName(), command);
        }
    }

    public Either<UserError, Command> getCommand(
        String commandName
    ) {
        val cmd = this.map.get(commandName);
        if (cmd == null) {
            return Either.left(UserError.create("Unknown command"));
        }
        return Either.right(cmd);
    }

    public Either<UserError, Object> execute(
        ReadyCommand readyCommand
    ) {
        return getCommand(readyCommand.getCommandDef().getName())
            .flatMap(cmd -> cmd
                .executeBlocking(readyCommand.getMethodContext())
            );
    }

}
