package jkit.jwt;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.Test;

class JwtModelTest {

    @Value(staticConstructor = "of")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    static class User {
        String name;
        String email;
    }

    JwtHMAC hmac = JwtHMAC.create("test");
    JwtModel<User> model = JwtModel.of(
        hmac, User.class, "arr", "jcUser"
    );

    @Test
    void sign() {

        val obj = json.createEmptyObject()
            .put("name", "alex");

        val actual = model.sign(new User("Alex", ""));

        assertTrue(actual.isRight());

    }

    @Test
    void verify() {

        val input = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwIjp7Im5hbWUiOiJhbGV4In0sImlzcyI6ImpjIn0.OzI-CWFCyznXERFCycMGoj877w6JIE8MtLStgf3hJE0";

        val actual = model.decode(input);

        assertTrue(actual.isRight());

    }

}