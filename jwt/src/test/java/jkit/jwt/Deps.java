package jkit.jwt;

import jkit.jackson.JacksonMain;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.ValidatorImpl;
import lombok.EqualsAndHashCode;
import lombok.Value;

public interface Deps {

    @Value(staticConstructor = "of")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class User {
        String name;
        String email;
    }

    JacksonMain<ObjectMapperExt> jacksonMain = JacksonMain.create(ValidatorImpl.of());
    ObjectMapperExt json = jacksonMain.getJson();

    JwtHMAC hmac = JwtHMAC.create(json, "test");
    JwtModel<User> model = JwtModel.of(
        User.class, hmac, "alex", "user"
    );

}
