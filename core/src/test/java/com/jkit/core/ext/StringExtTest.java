package com.jkit.core.ext;

import lombok.var;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringExtTest {

    @Test
    void snake2camel() {
        assertEquals(StringExt.snake2camel("USER_ID"), "userId");
        assertEquals(StringExt.snake2camel("user_name"), "userName");
        assertEquals(StringExt.snake2camel("my_first_word"), "myFirstWord");
    }

    @Test
    void camel2case() {
        assertEquals(StringExt.camel2snake("userName"), "user_name");
        assertEquals(StringExt.camel2snake("myFirstWord"), "my_first_word");
    }

    @Test
    void testDecodeBase64() {
        var actual = StringExt.decodeBase64("YXJycg").get();
        var actual3 = StringExt.decodeBase64("YXJy").get();
        var actual4 = StringExt.decodeBase64("YXI").get();
        var actual2 = StringExt
            .decodeBase64("eyJzYXBVc2VySWQiOiJkMDM3NDI5Iiwic2Vzc2lvbklkIjoiMmUyMGE2NTgtNDI5Mi00MjU2LWIxN2MtZWUwNmM3ZTBjNDg0In0").get();

        assertEquals(actual, "arrr");
        assertEquals(actual3, "arr");
        assertNotEquals(actual2, "");
        assertEquals(actual4, "ar");
    }

    @Test
    void limitLength() {
        assertEquals(StringExt.limitLength("Alexander", 4), "Alex");
        assertEquals(StringExt.limitLength("Alexander", 15), "Alexander");
    }

}