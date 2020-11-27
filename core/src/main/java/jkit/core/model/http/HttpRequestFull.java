//package jkit.core.model.http;
//
//import io.vavr.collection.Map;
//import io.vavr.control.Either;
//import jkit.core.model.UserError;
//import lombok.*;
//
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@Value(staticConstructor = "create")
//public class HttpRequestFull {
//
//    Map<String, Object> queryParams;
//    Map<String, Object> headers;
//    Map<String, Object> cookie;
//    Map<String, Object> payload;
//
//    public Either<UserError, String> getParam(String key, Map<String, Object> params, String domain) {
//        return jsonDSL.getPath(
//            params,
//            key,
//            v -> Either.right(v.asText())
//        ).mapLeft(e -> UserError
//            .create(String.format("Can't get %s param", domain))
//            .withErrors(List.of(e))
//        );
//    }
//
//    public Either<UserError, String> getQueryParam(String key) {
//        return getParam(key, queryParams, "query");
//    }
//
//    public Either<UserError, String> getHeaderParam(String key) {
//        return getParam(key, headers, "header");
//    }
//
//    public <B> Either<UserError, B> getQueryParams(Class<B> clazz) {
//
//        return json
//            .deserialize(queryParams, clazz);
//
//    }
//
//}
