package jkit.core.ext;

import io.vavr.control.*;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public interface SecureExt {

    static Try<PrivateKey> str2PrivateKey(
        KeyFactory keyFactory, String privateKeyBase64
    ) {
        return TryExt.get(() -> {
            KeySpec key = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyBase64));
            return keyFactory.generatePrivate(key);
        }, "str2PrivateKey");
    }

    static Try<KeyFactory> createKeyFactory() {
        return TryExt.get(
            () -> KeyFactory.getInstance("RSA"),
            "get key instance"
        );
    }

}
