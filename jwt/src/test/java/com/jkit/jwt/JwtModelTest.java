package com.jkit.jwt;

import io.vavr.control.Try;
import lombok.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtModelTest implements Deps {

    User user = User.of("alex", "alex@gmail.com");
    String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhbGV4IiwidXNlciI6eyJuYW1lIjoiYWxleCIsImVtYWlsIjoiYWxleEBnbWFpbC5jb20ifX0.R1nXw41V9ffrs-sGUvTBBJmVE5M07KZSj0og8bOE53I";

    @Test
    void sign() {

        val actual = model.sign(user);

        assertEquals(Try.success(jwt), actual);

    }

    @Test
    void verify() {

        val actual = model.decode(jwt);

        assertEquals(Try.success(User.of("alex", "")), actual);

    }

}