package jkit.akka_http;

import io.vavr.control.Either;
import jkit.core.model.UserError;

public interface IHttpConfig {
    Either<UserError, Integer> getHttpPort();
    String getServiceName();
}
