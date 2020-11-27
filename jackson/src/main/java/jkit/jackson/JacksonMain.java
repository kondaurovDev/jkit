package jkit.jackson;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Either;
import jkit.core.iface.Validator;
import jkit.core.model.DataFormat;
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

    public A getDataMapper(DataFormat dataFormat) {
        if (dataFormat == DataFormat.json) return json;
        if (dataFormat == DataFormat.yaml) return yaml;
        throw new Error("Unknown data format");
    }

    public Either<UserError, HashMap<String, Object>> readPayload(
        String body,
        DataFormat dataFormat
    ) {
        return getDataMapper(dataFormat)
            .deserializeToMap(body, Object.class);
    }

}
