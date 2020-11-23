package jkit.core.model;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.control.Either;
import io.vavr.control.Option;

public class FieldMap<A extends FieldMap.Key> {

    public final java.util.HashMap<A, Object> fields;

    public <T> Either<UserError, Option<T>> getOpt(A key, Class<T> tClass) {
        return Option
            .of(fields.get(key))
            .toEither(() -> UserError.create("Key not found " + key.getKey()))
            .map(tClass::cast)
            .map(Option::of)
            .mapLeft(e -> e.withError("Can't get key " + key.getKey()));
    }

    public <T> Either<UserError, T> get(A key, Class<T> tClass) {
        return getOpt(key, tClass)
            .flatMap(o -> o.
                toEither(() -> UserError.create("Got null value in key" + key.getKey()))
            );
    }

    public FieldMap(Iterable<Tuple2<A, Object>> fields) {
        this.fields = new java.util.HashMap<>();
        fields.forEach(t -> this.fields.put(t._1, t._2));
    }

    public java.util.Map<String, Object> toMap() {
        return HashMap
            .ofAll(fields)
            .toJavaMap(t -> Tuple.of(
                t._1.getKey(),
                t._2
            ));
    }

//    public Either<UserError, JsonNode> toJsonNode() {
//        return json.toJsonNode(toMap());
//    }
//
//    public <C> Either<UserError, C> toClass(Class<C> clazz) {
//        return toJsonNode()
//            .flatMap(jsonS -> json.deserialize(jsonS, clazz));
//    }

    public interface FieldMapTransformer<F extends Key, R> {
        Either<UserError, R> apply(FieldMap<F> fieldMap);
    }

    public interface Key {
        String getKey();
    }

}