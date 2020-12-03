package jkit.entry;

import io.vavr.control.Either;
import jkit.core.iface.Entry;
import jkit.core.model.UserError;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReadyCommand {

    PropMap input;
    Entry.ICommandDef commandDef;

    public Either<UserError, Object> execute(
        Entry.IPropMap user,
        CommandMap commandMap
    ) {

        return commandMap
            .getCommand(commandDef.getName())
            .flatMap(command ->
                command.executeBlocking(
                    MethodContext.of(
                        input,
                        user,
                        new UserLog()
                    )
                )
            );

    }

}
