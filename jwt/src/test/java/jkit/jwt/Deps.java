package jkit.jwt;

import jkit.jackson.JacksonMain;
import jkit.jackson.JKitJackson;
import jkit.validate.Validator;
import lombok.EqualsAndHashCode;
import lombok.Value;

public interface Deps {

    @Value(staticConstructor = "of")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class User {
        String name;
        String email;
    }

    JacksonMain<JKitJackson> jacksonMain = JacksonMain.create(Validator.of());
    JKitJackson json = jacksonMain.getJson();

    JwtHMAC hmac = JwtHMAC.create(json, "test");
    JwtModel<User> model = JwtModel.of(
        User.class, hmac, "alex", "user"
    );

}
