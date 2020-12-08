package jkit.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.vavr.control.Either;
import io.vavr.jackson.datatype.VavrModule;
import jkit.core.model.UserError;
import jkit.jackson.serdes.LocalDateTimeSerde;
import jkit.jackson.serdes.LocalTimeSerde;
import lombok.val;

public interface JacksonModule {

    interface IJsonTypeTransformer<A> {
        Either<UserError, A> apply(JsonNode json);
    }

    interface IJsonTransformer {
        JsonNode apply(JsonNode json);
    }

    interface IJsonFactory {
        Either<UserError, JsonNode> apply();
    }

    static void configureObjectMapper(ObjectMapper mapper) {
        mapper
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, true);

        val jodaModule = new JodaModule();

        LocalDateTimeSerde.register(jodaModule);
        LocalTimeSerde.register(jodaModule);

        VisibilityChecker<?> checker = mapper
            .getSerializationConfig()
            .getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
            .withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withCreatorVisibility(JsonAutoDetect.Visibility.ANY);

        mapper
            .registerModule(new VavrModule())
            .registerModule(jodaModule)
            .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mapper.setVisibility(checker);
    }

    static ObjectMapper createJsonMapper() {
        val objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

    static ObjectMapper createYamlMapper() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

}
