package jkit.entry;

import io.vavr.control.Either;
import jkit.core.model.UserError;
import lombok.*;

import java.util.HashMap;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommandMap<U> {

    HashMap<String, Command<U>> map;

    public static <U> CommandMap<U> create() {
        return CommandMap.of(
            new HashMap<>()
        );
    }

    public Command<U> create(
        CommandDef commandDef,
        IApi.AccessChecker<U> accessChecker,
        IApi.Executor<U> executor
    ) {
        val cmd = Command.of(
            commandDef,
            accessChecker,
            executor
        );
        this.register(cmd);
        return cmd;
    }

    public void register(Command<U> command) {
        if (map.containsKey(command.getCommandDef().getName())) {
            throw new Error("Command '" + command.getCommandDef().getName() + "' has been registered already");
        } else {
            map.put(command.getCommandDef().getName(), command);
        }
    }

    public Either<UserError, Command<U>> getCommand(
        String commandName
    ) {
        val cmd = this.map.get(commandName);
        if (cmd == null) {
            return Either.left(UserError.create("Unknown command"));
        }
        return Either.right(cmd);
    }

    public Either<UserError, IApi.IResponse> execute(
        String commandName,
        ExecuteCmdRequest<U> request
    ) {
        return getCommand(commandName)
            .flatMap(cmd -> cmd
                .createResponse(
                    request
                )
            );
    }

}
