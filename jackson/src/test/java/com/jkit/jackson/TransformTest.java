package com.jkit.jackson;

import io.vavr.collection.HashMap;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransformTest implements Deps {

    @Test
    void transformMapTest() {

        val actual = json.objToStringMap(
            HashMap.of(
                "int", 1,
                "product", HashMap.of("name", "alex"),
                "animal", "dog",
                "float", 3.14,
                "bool", false
            ).toJavaMap()
        );

        assertFalse(actual.isEmpty());

    }

}
