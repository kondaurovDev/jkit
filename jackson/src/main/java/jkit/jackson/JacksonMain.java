package jkit.jackson;

import jkit.core.iface.Validator;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JacksonMain<A extends ObjectMapperExt> {

    IDsl jsonDSL;
    A json;
    A yaml;

    public static JacksonMain<ObjectMapperExt> create(Validator validator) {
        return JacksonMain.of(
            new IDsl() {},
            ObjectMapperExt.of(JacksonModule.createJsonMapper(), validator),
            ObjectMapperExt.of(JacksonModule.createYamlMapper(), validator)
        );
    }

}
