package jkit.core.ext;

import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.lang.reflect.Field;

public interface EnvExt {

    static Try<String> getEnv(String name) {
        return getEnvOpt(name)
            .toTry(() ->
                new Error(String.format("Environment variable '%s' not defined", name))
            );

    }

    static Option<String> getEnvOpt(String name) {
        return Option
            .of(System.getenv(name));
    }

    static <A> Map<String, Option<String>> getEnvMap(
        Class<A> clazz, String paramPrefix
    ) {
        return List.of(clazz.getFields())
            .map(Field::getName)
            .toMap(f -> Tuple.of(f, getEnvOpt(paramPrefix + f)));
    }
}
