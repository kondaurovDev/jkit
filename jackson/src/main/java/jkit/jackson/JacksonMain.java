package jkit.jackson;

import io.vavr.collection.HashMap;
import io.vavr.control.Either;
import jkit.core.iface.Entry;
import jkit.core.iface.Validator;
import jkit.core.model.UserError;
import lombok.*;

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

    public A getDataMapper(Entry.DataFormat dataFormat) {
        if (dataFormat == Entry.DataFormat.JSON) return json;
        if (dataFormat == Entry.DataFormat.YAML) return yaml;
        throw new Error("Unknown data format");
    }

    public Either<UserError, HashMap<String, Object>> readPayload(
        String body,
        Entry.DataFormat dataFormat
    ) {
        return getDataMapper(dataFormat)
            .deserializeToMap(body, Object.class);
    }

}
