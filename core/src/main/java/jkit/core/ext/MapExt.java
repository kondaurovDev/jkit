package jkit.core.ext;

import io.vavr.control.Either;
import jkit.core.model.UserError;
import java.util.Map;
import java.util.stream.Collectors;

public interface MapExt {

    static <A> Either<UserError, A> get(
        String key,
        Map<String, A> map,
        String error
    ) {
        return VavrExt.checkNull(map.get(key), error);
    }

    static String map2yaml(Map<String, String> map) {
        return map.entrySet().stream()
            .map(t -> String.format("%s=%s", t.getKey(), t.getValue()))
            .collect(Collectors.joining("/n"));
    }

}
