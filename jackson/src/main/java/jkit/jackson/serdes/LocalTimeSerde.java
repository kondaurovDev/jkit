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
import org.joda.time.LocalTime;

import java.io.IOException;

public interface LocalTimeSerde {

    static void register(SimpleModule module) {
        module.addDeserializer(LocalTime.class, new Des(LocalTime.class));
        module.addSerializer(new Ser(LocalTime.class));
    }

    class Ser extends StdSerializer<LocalTime> {

        public Ser(Class<LocalTime> t) {
            super(t);
        }

        @Override
        public void serialize(LocalTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            val s = value.toString(TimeExt.timeFormat);
            jgen.writeString(s);
        }

    }

    class Des extends StdDeserializer<LocalTime> {

       public Des(Class<LocalTime> t) {
            super(t);
        }

        @Override
        public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            if (node.isLong()) {
                return new LocalTime(node.longValue());
            }

            String s = node.asText();
            val lt = LocalTime.parse(s);
            return lt;
        }
    }
}
