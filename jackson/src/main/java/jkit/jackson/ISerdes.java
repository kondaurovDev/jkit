package jkit.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

interface ISerdes {

    interface Serialize<A> {
        String serialize(A input);
    }

    interface Deserialize<A> {
        A deserialize(JsonNode input);
    }

    default <A> void createSerde(
        SimpleModule module,
        Class<A> aClass,
        Serialize<A> serialize,
        Deserialize<A> deserialize
    ) {
        createSerializer(module, aClass, serialize);
        createDeserializer(module, aClass, deserialize);
    }

    static<A> void createSerializer(
        SimpleModule module,
        Class<A> aClass,
        Serialize<A> serialize
    ) {
        module.addSerializer(createSerializer(
            aClass,
            serialize
        ));
    }

    static<A> void createDeserializer(
        SimpleModule module,
        Class<A> aClass,
        Deserialize<A> deserialize
    ) {
        module.addDeserializer(aClass, createDeserializer(
            aClass,
            deserialize
        ));
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
