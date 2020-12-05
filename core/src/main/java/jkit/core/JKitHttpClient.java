package jkit.core;

import io.vavr.Tuple2;
import io.vavr.collection.List;

public interface JKitHttpClient {

    interface IHttpResponse<A> {
        Integer getCode();
        String getCodePhrase();
        A getBody();
        List<Tuple2<String, String>> getHeaders();
    }

}
