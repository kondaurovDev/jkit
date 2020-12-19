package com.jkit.jwt;

import com.jkit.jackson.JKitJackson;
import com.jkit.validate.Validator;
import lombok.*;

public interface Deps {

    @Value(staticConstructor = "of")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class User {
        String name;
        String email;
    }

    JKitJackson json = JKitJackson.create(
        Validator.of(),
        (a, b, c) -> c.configureObjectMapper(b)
    );

    JwtHMAC hmac = JwtHMAC.create(json, "test");
    JwtModel<User> model = JwtModel.of(
        User.class, hmac, "alex", "user"
    );

}
