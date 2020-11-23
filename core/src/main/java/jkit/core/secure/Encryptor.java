package jkit.core.secure;

import io.vavr.control.Either;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.var;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Encryptor {

    Cipher encryptCipher;
    Cipher decryptCipher;

    public static Either<UserError, Encryptor> create(
        String key
    ) {

        return buildKey(key).flatMap(secretKeySpec ->
            buildCipher(secretKeySpec, Cipher.ENCRYPT_MODE)
                .flatMap(encryptCipher ->
                    buildCipher(secretKeySpec, Cipher.DECRYPT_MODE)
                        .map(decryptCipher ->
                             new Encryptor(
                                 encryptCipher,
                                 decryptCipher
                             )
                        )
                )
        );

    }

    static Either<UserError, Cipher> buildCipher(
        SecretKeySpec secretKey,
        int mode
    ) {
        return TryExt.get(() -> {
            var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(mode, secretKey);
            return cipher;
        }, "build cipher");
    }

    static Either<UserError, SecretKeySpec> buildKey(String myKey) {
        return TryExt.get(() -> {
            MessageDigest sha;
            byte[] key;
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");
        }, "build secret key");
    }

    public Either<UserError, String> encrypt(
        String strToEncrypt
    ) {

        return TryExt.get(() ->
            encryptCipher
                .doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)),
            "encrypt string"
        ).map(r -> Base64.getEncoder().encodeToString(r));

    }

    public Either<UserError, String> decrypt(
        String str
    ) {

        return TryExt.get(() ->
            decryptCipher.doFinal(Base64.getDecoder().decode(str)),
            "decrypt string"
        ).map(String::new);

    }

}
