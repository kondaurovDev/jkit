package jkit.entry.akka_http;

import akka.http.javadsl.server.AllDirectives;
import com.jkit.akka_http.route.ICommandRoute;
import com.jkit.core.JKitData;
import io.vavr.control.Try;
import jkit.entry.Command;
import jkit.entry.CommandDef;
import jkit.entry.CommandEvent;
import jkit.entry.CommandMap;
import com.jkit.jwt.JwtHMAC;
import lombok.*;

import java.util.function.Consumer;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
class Router extends AllDirectives implements ICommandRoute {

    CommandMap commandMap;
    JwtHMAC jwtHMAC;
    String jwtClaimName;
    JKitData.IObjMapper<?> objMapperMain;

    public Consumer<CommandEvent> onCommandExecute() {
        return null;
    }

    public AllDirectives d() {
        return this;
    }

    public static Router create(
        JKitData.IObjMapper<?> objMapper
    ) {
        val commandMap = CommandMap.create();
        commandMap.register(
            Command.of(
                CommandDef.of("test"),
                u -> true,
                ctx -> Try.success("Test command")
            )
        );
        return new Router(
            commandMap,
            JwtHMAC.create(objMapper, "asd"),
            "user",
            objMapper
        );
    }
}