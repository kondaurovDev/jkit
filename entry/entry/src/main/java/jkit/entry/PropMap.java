package jkit.entry;

import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.ext.MapExt;
import jkit.core.iface.Entry;
import jkit.core.model.UserError;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

public class PropMap implements Entry.IPropMap {

    HashMap<String, Object> params = new HashMap<>();

    public static PropMap create() {
        return new PropMap();
    }

    public PropMap param(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public PropMap params(Map<String, Object> all) {
        params.putAll(all);
        return this;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public <A> Either<UserError, A> propOpt(Entry.IPropDef<A> param) {
        return MapExt
            .get(param.getName(), params,String.format("Param '%s'  not found", param.getName()))
            .flatMap(v -> {
                if (!param.getParamClass().isInstance(v))
                    return Either.left(UserError.create("Wrong class"));
                return Either.right(param.getParamClass().cast(v));
            });
    }

    public <A> A prop(Entry.IPropDef<A> param) {
        val v = propOpt(param);

        if (v.isEmpty())
            throw UserError.create(
                String.format(
                    "Param '%s' not found",
                    param.getName()
                )
            ).toError();

        return v.get();
    }

    public <A> List<A> paramList(PropDef<A> propDef) {
        return propDef
            .validateList(prop(propDef))
            .getOrElseThrow(UserError::toError);
    }

}
