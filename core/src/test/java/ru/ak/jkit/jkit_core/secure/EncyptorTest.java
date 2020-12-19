package ru.ak.jkit.jkit_core.secure;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncyptorTest {

    @Test
    void case1() {

        val encyptor = Encryptor.create("super").get();

        val res = encyptor.encrypt("heh")
            .flatMap(encyptor::decrypt).get();

        assertEquals(res, "heh");

    }

}