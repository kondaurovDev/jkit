package jkit.entry.akka_http;

import io.vavr.collection.HashMap;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest implements Deps {

    @Test
    void bind() {
        val router = Router.create(jacksonMain);

        val actual = Main.bind(router);

        val req =
            httpClient
                .createPostRequest("http://localhost:8080/api")
                .flatMap(r -> httpClient.createJsonEntity(
                        HashMap.of(
                            "command", "test",
                            "payload", "{}"
                        )
                    )
                    .map(e -> {
                        r.setEntity(e);
                        return r;
                    })
                ).flatMap(httpClient::getJsonResponse);

        assertTrue(actual.isRight());
    }

}