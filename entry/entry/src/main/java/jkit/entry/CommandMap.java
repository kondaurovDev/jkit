package jkit.entry;

import io.vavr.control.Try;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommandMap {

    Map<String, Command> map;

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

    public Try<Command> getCommand(
        String commandName
    ) {
        val cmd = this.map.get(commandName);
        if (cmd == null) {
            return Try.failure(new Error("Unknown command"));
        }
        return Try.success(cmd);
    }

    public Try<ReadyCommand> getReadyCommand(
        CommandRequest commandRequest
    ) {
        return getCommand(commandRequest.getCommandName())
            .flatMap(cmd -> cmd.getCommandDef().createReadyCommand(
                PropMap.create().props(commandRequest.getPayload()).build(),
                PropMap.create().props(commandRequest.getUser()).build()
            ));
    }

    public Try<Object> execute(
        ReadyCommand readyCommand
    ) {
        return getCommand(readyCommand.getCommandDef().getName())
            .flatMap(cmd -> cmd
                .executeBlocking(readyCommand.getMethodContext())
            );
    }

}
