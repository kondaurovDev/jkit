package jkit.entry;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jkit.core.iface.Entry;
import jkit.core.model.UserError;
import lombok.*;

import java.util.ArrayList;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MethodContext<U> implements Entry.IMethodContext<U> {
    Map<CommandParam<?>, ?> params;
    U user;
    Entry.IUserLog userLog;
    ArrayList<String> logHistory = new ArrayList<>();

    public void log(String msg) {
        this.userLog.add(msg);
        this.logHistory.add(msg);
    }

    public Option<?> paramValueOpt(CommandParam<?> param) {
        return params.get(param);
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
            .map(v -> commandParam.getAClass().cast(v));
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

    public <A> Either<UserError, A> params(
        Class<A> aClass,
        String... fields
    ) {
        return json.convert(params, aClass, fields);
    }

    public String getLogs() {
        return String.join("\n", this.logHistory);
    }

}
