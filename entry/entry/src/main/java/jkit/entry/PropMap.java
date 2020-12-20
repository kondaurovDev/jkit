package jkit.entry;

import io.vavr.collection.List;
import io.vavr.control.Try;
import com.jkit.core.ext.MapExt;
import lombok.*;

import java.util.Map;

@Value
@Builder(builderMethodName = "create")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PropMap {

    @Singular
    Map<String, Object> props;

    public Map<String, Object> getParams() {
        return this.props;
    }

    public <A> Try<A> propOpt(PropDef<A> prop) {
        return MapExt
            .get(prop.getName(), props, String.format("Param '%s' not found", prop.getName()))
            .flatMap(v -> {
                if (!prop.getParamClass().isInstance(v))
                    return Try.failure(new Error("Wrong class"));
                return Try.success(prop.getParamClass().cast(v));
            });
    }

    public <A> A prop(PropDef<A> prop) {
        val v = propOpt(prop);

        if (v.isEmpty())
            throw JKitError.create(
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
            .getOrElseThrow(JKitError::toError);
    }

}
