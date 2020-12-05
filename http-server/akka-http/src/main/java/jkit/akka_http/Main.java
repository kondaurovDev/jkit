package jkit.akka_http;

public interface Main {

    static void main(String[] args) {
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
