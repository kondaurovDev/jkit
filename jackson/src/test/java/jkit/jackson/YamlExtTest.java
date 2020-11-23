package jkit.jackson;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YamlExtTest implements Deps {

    @Test
    void parse() {

        val actual = yaml.mapToYamlAndParse(HashMap.ofEntries(
            Tuple.of("someBool", "true"),
            Tuple.of("arr", "[1,2,3]"),
            Tuple.of("str", "Hey")
        ));

        assertTrue(actual.isRight());

    }

    @Test
    void parse2() {

        val actual = yaml.mapToYamlAndParse(HashMap.empty());

        assertTrue(actual.isRight());

    }

}