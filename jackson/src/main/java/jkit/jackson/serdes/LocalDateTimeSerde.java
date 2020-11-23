package jkit.jackson.serdes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jkit.core.ext.TimeExt;
import lombok.val;
import org.joda.time.LocalDateTime;

import java.io.IOException;

public interface LocalDateTimeSerde {

    static void register(SimpleModule module) {
        module.addDeserializer(LocalDateTime.class, new Des(LocalDateTime.class));
        module.addSerializer(new Ser(LocalDateTime.class));
    }

    class Ser extends StdSerializer<LocalDateTime> {

        public Ser(Class<LocalDateTime> t) {
            super(t);
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            val s = value.toString(TimeExt.dateFormat);
            jgen.writeString(s);
        }

    }

    class Des extends StdDeserializer<LocalDateTime> {

       public Des(Class<LocalDateTime> t) {
            super(t);
        }

        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            if (node.isLong()) {
                return new LocalDateTime(node.longValue());
            } else {
                throw new Error("Unknown input for date");
            }
        }
    }
}
