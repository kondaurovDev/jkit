package com.jkit.core;

import io.vavr.control.Try;

public interface JKitValidate {

    interface IValidator {
        <A> Try<A> validate(A input, String... fields);
    }

}
