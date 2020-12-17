package jkit.jackson;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Try;
import jkit.core.ext.StringExt;
import lombok.*;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JsonExtTest implements Deps {

    @Test
    void parseJson() {

        val input = "{\"f\": 123, \"name\": \"Alexxx\"}";

        var actual = json.parseRaw(input);

        assertTrue(actual.isSuccess());

    }

    @Test
    void createObjectNode() {

        val actual = json.createObjectNode(List.of(
            Tuple.of("bla", 1),
            Tuple.of("strProp", "str"),
            Tuple.of("someBool", false)
        )).toString();

        val expected = "{\"bla\":1,\"strProp\":\"str\",\"someBool\":false}";

        assertEquals(expected, actual);

    }

    @Test
    void convertTest() {

        val actual = json.convert(
            HashMap.of(
                "userName", "alex",
                "nums", List.of(1,2,3)
            ),
            MyUser2.class
        );

        assertTrue(actual.isSuccess());

    }

    @Test
    void deserializeFromJson() {

        val node = json
            .createEmptyObject()
            .put("userId", 1)
            .put("groups", true)
            .put("userName", "Alexander");

        node.putArray("groups");

        val actual = json
            .deserialize(node, MyUser.class);

        assertEquals(actual.map(MyUser::getUserName), Try.success("Alexander"));

    }

    @Test
    void deserializeFromJson2() {

        val node =
            "{\"userId\":5,\"swaExists\":true,\"userName\":\"Alex\", \"groups\": []}";

        val row = json
            .deserialize(node, MyUser.class);

        assertTrue(row.isSuccess());

    }

    @Test
    void transformKeys() {

        val originJson = json
            .createEmptyObject()
            .put("ID", 1)
            .put("EMAIL", "str")
            .put("USER_NAME", "Alex");

        val expectedJson = json
            .createEmptyObject()
            .put("id", 1)
            .put("email", "str")
            .put("userName", "Alex");

        val actual = json
            .transformKeys(originJson, StringExt::snake2camel);

        assertEquals(actual, expectedJson);

    }

    @Test
    void mergeTest() {

        val node1 = json.newObj()
            .put("name", "foo");

        node1.putObject("obj")
            .put("opt1", false)
            .put("opt2", 123);

        val node2 = json.newObj()
            .put("name", "bar");

        node2.putObject("obj")
            .put("opt1", true)
            .put("opt2", "bla")
            .put("opt3", "arr");

        val merged = json.merge(node1, node2);

        assertTrue(merged.isSuccess());

        val name = json.getPath(
            merged.get(), "name"
        );

        assertEquals(name.get().asText(), "bar");

    }

    @Test
    void deserializeEither() {

        val input =
            "{\n" +
                "\"option1\": \"1234\",\n" +
                "\"option2\": \"123\",\n" +
                "\"option3\": true\n" +
            "}";

        val actual = json.deserialize(input, EitherTest.class);

        assertTrue(actual.isSuccess());

    }

    @Test
    void serialize() {

        val t = json.toJsonNode(LocalTime.parse("20:05:01")).get().toString();

        val a = json.toJsonNode(t);

    }

    @Test
    void nodeToMapTest() {

        val node = json.createEmptyObject()
            .put("key1", 1)
            .put("b", true)
            .put("num", 5);

        val actual = json
            .objToMap(node);

        val a = 1;

    }

    @Test
    void fromEnvParams() {

        var a = List
            .of(MyTestConfig.class.getDeclaredFields())
            .map(Field::getName);

        var b = 1;

    }

    @Test
    void parseGeneric() {

        val a = json.deserializeToList("[1,2,3]", Integer.class);
        val b = json.deserializeToMap("{\"num\":[1,2,3], \"o\": {\"a\": 1}}", Object.class);
        val c = json.deserialize("{\"num\":1}", Object.class);
        val d = json.deserialize("\"some str\"", String.class);
        val e = json.deserialize("{\"nums\": [1,2,3]}", MyUser2.class);

        val _b = 1;

    }

    @Test
    void parseEnum() {

        val d = json.deserialize("\"create\"", CrudAction.class);

        assertTrue(d.isSuccess());

    }

}