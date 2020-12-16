package jkit.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.vavr.control.Either;
import io.vavr.jackson.datatype.VavrModule;
import jkit.core.model.UserError;
import lombok.val;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

interface IFactory extends ISerdes  {

    interface IJsonTypeTransformer<A> {
        Either<UserError, A> apply(JsonNode json);
    }

    interface IJsonTransformer {
        JsonNode apply(JsonNode json);
    }

    default void configureObjectMapper(ObjectMapper mapper) {
        mapper
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, true);

        val simpleModule = new SimpleModule();

        createSerde(
            simpleModule,
            LocalDateTime.class,
            dt -> dt.toString("dd/MM/YY HH:mm:ss"),
            node -> {
                if (node.isLong()) {
                    return new LocalDateTime(node.longValue());
                } else {
                    throw new Error("Unknown input for date time");
                }
            }
        );

        createSerde(
            simpleModule,
            LocalTime.class,
            dt -> dt.toString("HH:mm:ss"),
            node -> {
                if (node.isLong()) {
                    return new LocalTime(node.longValue());
                }

                String s = node.asText();
                return LocalTime.parse(s);
            }
        );

        VisibilityChecker<?> checker = mapper
            .getSerializationConfig()
            .getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
            .withGetterVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withCreatorVisibility(JsonAutoDetect.Visibility.ANY);

        mapper
            .registerModule(new VavrModule())
            .registerModule(new JodaModule())
            .registerModule(simpleModule)
            .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mapper.setVisibility(checker);
    }

    default ObjectMapper createJsonMapper() {
        val objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

    default ObjectMapper createYamlMapper() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

}
