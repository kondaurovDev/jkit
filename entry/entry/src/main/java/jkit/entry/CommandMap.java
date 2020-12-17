package jkit.entry;

import io.vavr.control.Either;
import jkit.core.JKitEntry;
import jkit.core.model.JKitError;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommandMap implements JKitEntry.ICommandMap {

    Map<String, JKitEntry.ICommand> map;

    public static CommandMap create() {
        return CommandMap.of(
            new HashMap<>()
        );
    }

    public void register(Command command) {
        if (map.containsKey(command.getCommandDef().getName())) {
            throw new java.lang.Error("Command '" + command.getCommandDef().getName() + "' has been registered already");
        } else {
            map.put(command.getCommandDef().getName(), command);
        }
    }

    public Either<JKitError, JKitEntry.ICommand> getCommand(
        String commandName
    ) {
        val cmd = this.map.get(commandName);
        if (cmd == null) {
            return Either.left(JKitError.create("Unknown command"));
        }
        return Either.right(cmd);
    }

    public Either<JKitError, JKitEntry.IReadyCommand> getReadyCommand(
        JKitEntry.ICommandRequest commandRequest
    ) {
        return getCommand(commandRequest.getCommandName())
            .flatMap(cmd -> cmd.getCommandDef().createReadyCommand(
                PropMap.create().props(commandRequest.getPayload()).build(),
                PropMap.create().props(commandRequest.getUser()).build()
            ));
    }

    public Either<JKitError, Object> execute(
        JKitEntry.IReadyCommand readyCommand
    ) {
        return getCommand(readyCommand.getCommandDef().getName())
            .flatMap(cmd -> cmd
                .executeBlocking(readyCommand.getMethodContext())
            );
    }

}
