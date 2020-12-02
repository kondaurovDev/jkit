package jkit.entry;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Either;
import jkit.core.iface.Entry;
import jkit.core.model.UserError;
import lombok.*;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PropMap implements Entry.IPropMap {

    Map<String, Object> params;

    public static PropMap create() {
        return PropMap.create();
    }

    public <A> Either<UserError, A> paramValueOpt(Entry.IPropDef<A> param) {
        return params.get(param.getName())
            .toEither(UserError.create("Param not found"))
            .flatMap(v -> {
                if (!param.getParamClass().isInstance(v))
                    return Either.left(UserError.create("Wrong class"));
                return Either.right(param.getParamClass().cast(v));
            });
    }

    public <A> A paramValue(PropDef<A> param) {
        val v = paramValueOpt(param);

        if (v.isEmpty()) {
            throw CommandError.ParamError.of(param, "Not found");
        }

        return v.get();
    }

    public <A> Either<UserError, A> paramOpt(PropDef<A> commandParam) {
        return paramValueOpt(commandParam)
            .toEither(() -> UserError.create(String.format("Param '%s' not found", commandParam.getName())))
            .map(v -> commandParam.getParamClass().cast(v));
    }

    public <A> A param(PropDef<A> commandParam) {
        return paramOpt(commandParam)
            .getOrElseThrow(UserError::toError);
    }

    public <A> List<A> paramList(PropDef<A> commandParam) {
        return commandParam
            .getList(paramValue(commandParam))
            .getOrElseThrow(UserError::toError);
    }

//    public <A> Either<UserError, A> params(
//        Class<A> aClass,
//        String... fields
//    ) {
//        return json.convert(params, aClass, fields);
//    }

}
