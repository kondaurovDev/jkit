package jkit.entry.akka_http;

import io.vavr.control.Either;
import jkit.akka_http.AkkaExt;
import jkit.core.model.UserError;

public interface Main {

    static Either<UserError, ?> bind(
        Router router
    ) {
        return AkkaExt.buildAndListen(
            "test",
            d -> d.concat(
                d.pathEndOrSingleSlash(() -> d.complete("index")),
                d.path("api", router::commandRoute)
            ),
            8080
        );
    }

    static void main(String[] args) {

    }

}
