package jkit.entry;

import io.vavr.control.Option;
import jkit.core.model.http.HttpRequestFull;
import lombok.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestFullTest {

    @Test
    void getCookiesFromString() {

        val cookies= HttpRequestFull.getCookiesFromString(
            Option.some("auth=YWRycg==;bla=YWRycg==")
        );

        val actual = cookies.get("auth");

        assertEquals(actual, "YWRycg==");

    }
}