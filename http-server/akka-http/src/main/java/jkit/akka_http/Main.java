package jkit.akka_http;

import lombok.val;

public interface Main {

    static void main(String[] args) {
        AkkaExt.buildAndListen(
            "test",
            d -> d.concat(
                d.pathEndOrSingleSlash(() -> d.complete("index")),
                d.path("file", () ->
                    d.extractRequestEntity(requestEntity -> {
                        val a = 1;
                        return d.complete("file upload");
                    })
                ),
                d.complete("Hello123")
            ),
            8080
        );
    }

}
