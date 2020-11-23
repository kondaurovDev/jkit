package jkit.core.ext;

import io.vavr.control.*;
import jkit.core.model.UserError;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public interface SecureExt {

    KeyFactory keyFactory = createKeyFactory();

    static Either<UserError, PrivateKey> str2PrivateKey(String privateKeyBase64) {
        return TryExt.get(() -> {
            KeySpec key = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyBase64));
            return keyFactory.generatePrivate(key);
        }, "str2PrivateKey");
    }

    static KeyFactory createKeyFactory() {
        return TryExt.getOrThrow(
            () -> KeyFactory.getInstance("RSA"),
            "get key instance"
        );
    }

}
