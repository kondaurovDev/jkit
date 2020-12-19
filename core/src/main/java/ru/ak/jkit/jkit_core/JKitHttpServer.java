package ru.ak.jkit.jkit_core;

import io.vavr.control.Try;

public interface JKitHttpServer {

    interface IHttpConfig {
        Try<Integer> getHttpPort();
    }

}
