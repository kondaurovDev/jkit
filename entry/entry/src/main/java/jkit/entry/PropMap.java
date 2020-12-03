package jkit.entry;

import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.ext.MapExt;
import jkit.core.iface.Entry;
import jkit.core.model.UserError;
import lombok.*;

@Value(staticConstructor = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class PropMap implements Entry.IPropMap {

    @Singular("param")
    java.util.Map<String, Object> params;

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
