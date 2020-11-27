package jkit.entry;

import io.vavr.collection.Map;
import io.vavr.control.Either;
import jkit.core.model.UserError;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReadyCommand {

    Map<CommandParam<?>, Object> input;
    String commandName;

    public <U> Either<UserError, Object> execute(
        U user,
        CommandMap<U> commandMap
    ) {

        return commandMap
            .getCommand(commandName)
            .flatMap(command ->
                command.execute(
                    MethodContext.of(
                        input,
                        user,
                        UserLog.stub
                    )
                )
            );

    }

//    public static Either<UserError, ReadyCommand> fromObject(
//        String commandName,
//        Object params
//    ) {
//
//        return json.nodeToMap(params)
//            .map(map -> ReadyCommand.of(map, commandName));
//
//    }


}
