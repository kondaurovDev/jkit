//package jkit.akka_http;
//
//import akka.http.javadsl.model.HttpResponse;
//import akka.http.javadsl.model.headers.HttpCookie;
//import io.vavr.collection.Stream;
//import io.vavr.control.Option;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//
//@Setter @Accessors(fluent = true)
//public class ResponseHolder implements IApi.IResponse {
//
//    HttpResponse response;
//    HttpCookie cookie;
//
//    public Stream<?> getResponse(
//        Consumer<CommandEvent> onSave
//    ) {
//        return router.d().setCookie(Option.of(cookie), () ->
//            Option.of(response).fold(
//                () -> router.d().complete("empty result"),
//                r -> router.d().complete(r)
//            )
//        );
//    }
//
//}