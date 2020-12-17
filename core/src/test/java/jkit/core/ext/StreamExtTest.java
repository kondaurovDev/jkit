package jkit.core.ext;

import io.vavr.collection.HashMap;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StreamExtTest {

    @Test
    void streamToMap() {

        val input = HashMap.of("Asd", 1).toJavaMap().entrySet().stream();

        val actual = StreamExt.streamToMap(input, k -> k.getKey().toLowerCase(), Map.Entry::getValue);

        val a = 1;

    }
}