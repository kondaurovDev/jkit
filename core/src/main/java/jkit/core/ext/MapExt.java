package jkit.core.ext;

import io.vavr.Function1;
import io.vavr.collection.*;
import io.vavr.control.Either;
import jkit.core.model.UserError;

public interface MapExt {

    static <A> Either<UserError, A> get(String key, Map<String, A> map, String error) {
        return map.get(key)
            .toEither(() -> UserError.create(error));
    }

    static <A> LinkedHashMap<String, A> createMap(List<A> lst, Function1<A, String> getKey) {
        return lst
            .collect(LinkedHashMap.collector(getKey));
    }

}
