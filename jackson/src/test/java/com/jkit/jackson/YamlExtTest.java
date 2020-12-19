//package jkit.jackson;
//
//import io.vavr.Tuple;
//import io.vavr.collection.HashMap;
//import lombok.val;
//import org.junit.jupiter.api.Test;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class YamlExtTest implements Deps {
//
//    @Test
//    void parse() {
//
//        val actual = json.mapToYmlAndParse(HashMap.ofEntries(
//            Tuple.of("someBool", "true"),
//            Tuple.of("arr", "[1,2,3]"),
//            Tuple.of("str", "Hey")
//        ).toJavaMap());
//
//        assertTrue(actual.isRight());
//
//    }
//
//    @Test
//    void parse2() {
//
//        val actual = jacksonMain
//            .mapToYmlAndParse(Collections.emptyMap());
//
//        assertTrue(actual.isRight());
//
//    }
//
//}