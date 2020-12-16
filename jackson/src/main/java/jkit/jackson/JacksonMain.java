package jkit.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Either;
import jkit.core.CorePredef;
import jkit.core.JKitData;
import jkit.core.JKitValidate;
import jkit.core.model.UserError;
import lombok.*;

import java.util.Map;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JacksonMain<A extends ObjectMapperExt>
    implements JKitData.IObjMapperMain<JsonNode, A> {

    private static IFactory factory = new IFactory() {};

    IDsl jsonDSL;
    A json;
    A yml;

    public static JacksonMain<ObjectMapperExt> create(JKitValidate.IValidator validator) {
        return JacksonMain.of(
            new IDsl() {},
            ObjectMapperExt.of(factory.createJsonMapper(), validator),
            ObjectMapperExt.of(factory.createYamlMapper(), validator)
        );
    }

    A getDataMapper(CorePredef.DataFormat dataFormat) {
        if (dataFormat == CorePredef.DataFormat.JSON) return json;
        if (dataFormat == CorePredef.DataFormat.YAML) return yml;
        throw new Error("Unknown data format");
    }

    public Either<UserError, Map<String, Object>> readPayload(
        String body,
        CorePredef.DataFormat dataFormat
    ) {
        return getDataMapper(dataFormat)
            .deserializeToMap(body, Object.class);
    }

}
