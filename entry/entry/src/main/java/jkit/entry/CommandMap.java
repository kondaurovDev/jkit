package jkit.entry;

import com.jkit.core.JKitEntry;
import io.vavr.control.Try;
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

    public void register(JKitEntry.ICommand command) {
        if (map.containsKey(command.getCommandDef().getName())) {
            throw new java.lang.Error("Command '" + command.getCommandDef().getName() + "' has been registered already");
        } else {
            map.put(command.getCommandDef().getName(), command);
        }
    }

    public Try<JKitEntry.ICommand> getCommand(
        String commandName
    ) {
        val cmd = this.map.get(commandName);
        if (cmd == null) {
            return Try.failure(new Error("Unknown command"));
        }
        return Try.success(cmd);
    }

    public Try<JKitEntry.IReadyCommand> getReadyCommand(
        JKitEntry.ICommandRequest commandRequest
    ) {
        return getCommand(commandRequest.getCommandName())
            .flatMap(cmd -> cmd.getCommandDef().createReadyCommand(
                PropMap.create().props(commandRequest.getPayload()).build(),
                PropMap.create().props(commandRequest.getUser()).build()
            ));
    }

    public Try<Object> execute(
        JKitEntry.IReadyCommand readyCommand
    ) {
        return getCommand(readyCommand.getCommandDef().getName())
            .flatMap(cmd -> cmd
                .executeBlocking(readyCommand.getMethodContext())
            );
    }

}
