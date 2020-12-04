package jkit.jackson;

import jkit.core.iface.IValidator;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JacksonMain<A extends ObjectMapperExt> {

    IDsl jsonDSL;
    A json;
    A yaml;

    public static JacksonMain<ObjectMapperExt> create(IValidator validator) {
        return JacksonMain.of(
            new IDsl() {},
            ObjectMapperExt.of(JacksonModule.createJsonMapper(), validator),
            ObjectMapperExt.of(JacksonModule.createYamlMapper(), validator)
        );
    }

}
