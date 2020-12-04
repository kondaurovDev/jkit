package jkit.entry.akka_http;

import akka.http.javadsl.server.AllDirectives;
import io.vavr.control.Either;
import jkit.core.JKitData;
import jkit.core.JKitEntry;
import jkit.entry.Command;
import jkit.entry.CommandDef;
import jkit.entry.CommandMap;
import jkit.jwt.JwtHMAC;
import lombok.*;

import java.util.function.Consumer;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
class Router extends AllDirectives implements ICommandRoute {

    JKitEntry.ICommandMap commandMap;
    JwtHMAC jwtHMAC;
    String jwtClaimName;
    JKitData.IObjMapperMain<?, ? extends JKitData.IObjMapper<?>> objMapperMain;

    public Consumer<JKitEntry.ICommandEvent> onCommandExecute() {
        return null;
    }

    public AllDirectives d() {
        return this;
    }

    public static Router create(
        JKitData.IObjMapperMain<?, ? extends JKitData.IObjMapper<?>> objMapperMain
    ) {
        val commandMap = CommandMap.create();
        commandMap.register(
            Command.of(
                CommandDef.of("test"),
                u -> true,
                ctx -> Either.right("Test command")
            )
        );
        return new Router(
            commandMap,
            JwtHMAC.create(objMapperMain.getJson(), "asd"),
            "user",
            objMapperMain
        );
    }
}