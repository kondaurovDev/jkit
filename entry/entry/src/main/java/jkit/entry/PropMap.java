package jkit.entry;

import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.ext.MapExt;
import jkit.core.JKitEntry;
import jkit.core.model.UserError;
import lombok.*;

import java.util.Map;

@Value
@Builder(builderMethodName = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PropMap implements JKitEntry.IPropMap {

    @Singular
    Map<String, Object> props;

    public Map<String, Object> getParams() {
        return this.props;
    }

    public <A> Either<UserError, A> propOpt(JKitEntry.IPropDef<A> prop) {
        return MapExt
            .get(prop.getName(), props, String.format("Param '%s' not found", prop.getName()))
            .flatMap(v -> {
                if (!prop.getParamClass().isInstance(v))
                    return Either.left(UserError.create("Wrong class"));
                return Either.right(prop.getParamClass().cast(v));
            });
    }

    public <A> A prop(JKitEntry.IPropDef<A> prop) {
        val v = propOpt(prop);

        if (v.isEmpty())
            throw UserError.create(
                String.format(
                    "Param '%s' not found",
                    prop.getName()
                )
            ).toError();

        return v.get();
    }

    public <A> List<A> propList(PropDef<A> propDef) {
        return propDef
            .validateList(prop(propDef))
            .getOrElseThrow(UserError::toError);
    }

}
