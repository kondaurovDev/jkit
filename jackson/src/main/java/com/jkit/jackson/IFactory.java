package com.jkit.jackson;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.vavr.control.Try;
import io.vavr.jackson.datatype.VavrModule;

import java.io.IOException;

public interface IFactory {

    interface MapperBuilder {
         ObjectMapper build(SimpleModule module, ObjectMapper mapper, IFactory factory);
    }

    interface IJsonTypeTransformer<A> {
        Try<A> apply(JsonNode json);
    }

    interface IJsonTransformer {
        JsonNode apply(JsonNode json);
    }

    interface Serialize<A> {
        String serialize(A input);
    }

    interface Deserialize<A> {
        A deserialize(JsonNode input);
    }

    default ObjectMapper createMapper() {
        return new ObjectMapper();
    }

    default ObjectMapper configureObjectMapper(ObjectMapper mapper) {
        mapper
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, true);

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
            .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mapper.setVisibility(checker);
        return mapper;
    }

    default <A> IFactory createSerde(
        SimpleModule module,
        Class<A> aClass,
        Serialize<A> serialize,
        Deserialize<A> deserialize
    ) {
        createSerializer(module, aClass, serialize);
        createDeserializer(module, aClass, deserialize);
        return this;
    }

    default <A> IFactory createSerializer(
        SimpleModule module,
        Class<A> aClass,
        Serialize<A> serialize
    ) {
        module.addSerializer(createSerializer(
            aClass,
            serialize
        ));
        return this;
    }

    default <A> IFactory createDeserializer(
        SimpleModule module,
        Class<A> aClass,
        Deserialize<A> deserialize
    ) {
        module.addDeserializer(aClass, createDeserializer(
            aClass,
            deserialize
        ));
        return this;
    }

    static<A> StdSerializer<A> createSerializer(
        Class<A> aClass,
        Serialize<A> serialize
    ) {
        return new StdSerializer<A>(aClass) {
            public void serialize(
                A a,
                JsonGenerator jsonGenerator,
                SerializerProvider serializerProvider
            ) throws IOException {
                jsonGenerator.writeString(serialize.serialize(a));
            }
        };
    }

    static<A> StdDeserializer<A> createDeserializer(
        Class<A> aClass,
        Deserialize<A> deserialize
    ) {
        return new StdDeserializer<A>(aClass) {
            public A deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                JsonNode node = jsonParser.getCodec().readTree(jsonParser);
                return deserialize.deserialize(node);
            }
        };
    }

}
