package jkit.akka_http;

import jkit.core.ext.IOExt;

public interface Main {

    static void main(String[] args) {
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
