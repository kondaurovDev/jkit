package jkit.entry;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jkit.core.iface.Entry;
import jkit.core.model.UserError;
import lombok.*;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParamMap implements Entry.IParamsMap {

    Map<String, ?> params;

    public static ParamMap create() {
        return ParamMap.create();
    }

    public Option<?> paramValueOpt(Entry.ICommandParam<?> param) {
        return params.get(param.getName());
    }

    public Object paramValue(CommandParam<?> param) {
        val v = paramValueOpt(param);

        if (v.isEmpty()) {
            throw CommandError.ParamError.of(param, "Not found");
        }

        return v.get();
    }

    public <A> Either<UserError, A> paramOpt(CommandParam<A> commandParam) {
        return paramValueOpt(commandParam)
            .toEither(() -> UserError.create(String.format("Param '%s' not found", commandParam.getName())))
            .map(v -> commandParam.getParamClass().cast(v));
    }

    public <A> A param(CommandParam<A> commandParam) {
        return paramOpt(commandParam)
            .getOrElseThrow(UserError::toError);
    }

    public <A> List<A> paramList(CommandParam<A> commandParam) {
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
