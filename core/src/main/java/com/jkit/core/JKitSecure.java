package com.jkit.core;

import io.vavr.control.Try;

import java.util.Map;

public interface JKitSecure {

    interface IJwtHMAC {
        Try<Map<String, Object>> verifyAndExtract(
            String token,
            String error
        );
    }

    interface SymmetricEncryptor {

        Try<String> encrypt(String input);
        Try<String> decrypt(String input);

    }

}
