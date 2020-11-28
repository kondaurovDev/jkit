package jkit.core.model.http;

import io.vavr.control.Either;
import jkit.core.model.UserError;

public interface IHttpConfig {
    Either<UserError, Integer> getHttpPort();
}
