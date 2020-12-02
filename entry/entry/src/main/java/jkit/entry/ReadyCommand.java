package jkit.entry;

import io.vavr.control.Either;
import jkit.core.model.UserError;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReadyCommand {

    PropMap input;
    String commandName;

    public <U> Either<UserError, Object> execute(
        U user,
        CommandMap<U> commandMap
    ) {

        return commandMap
            .getCommand(commandName)
            .flatMap(command ->
                command.executeBlocking(
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
