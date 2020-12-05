package jkit.core;

import io.vavr.control.Either;
import jkit.core.model.UserError;

public interface JKitHttpServer {

    interface IHttpConfig {
        Either<UserError, Integer> getHttpPort();
    }

}
