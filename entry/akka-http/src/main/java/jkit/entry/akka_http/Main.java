package jkit.entry.akka_http;

import jkit.akka_http.AkkaExt;
import jkit.core.ext.IOExt;
import jkit.entry.CommandMap;
import jkit.jwt.JwtHMAC;
import lombok.val;

public interface Main {

    static void main(String[] args) {
        val commandMap = CommandMap.create();
        val jwtHMAC = JwtHMAC.create()
        val router = Router.create(
            commandMap,

        );
        IOExt.out("Hey");
        AkkaExt.buildAndListen(
            "test",
            d -> d.concat(
                d.pathEndOrSingleSlash(() -> d.complete("index")),
                d.complete("Hello123")
            ),
            8080
        );
    }

}
