package jkit.entry.akka_http;

import akka.http.javadsl.server.AllDirectives;
import jkit.core.JKitData;
import jkit.core.JKitEntry;
import jkit.jwt.JwtHMAC;
import lombok.*;

import java.util.function.Consumer;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
class Router extends AllDirectives implements ICommandRoute {

    JKitEntry.ICommandMap commandMap;
    JwtHMAC jwtHMAC;
    String jwtClaimName;
    JKitData.IObjMapperMain<?, JKitData.IObjMapper<?>> objMapperMain;

    public Consumer<JKitEntry.ICommandEvent> onCommandExecute() {
        return null;
    }

    public AllDirectives d() {
        return this;
    }
}