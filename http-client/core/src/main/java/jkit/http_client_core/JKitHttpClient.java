package jkit.http_client_core;

import io.vavr.CheckedFunction1;
import io.vavr.control.Either;
import jkit.core.model.UserError;

public interface JKitHttpClient {

    interface IRequestFactory<A> {

    }

    interface IEntityFactory<A> {
        Either<UserError, ? extends A> createJsonEntity(Object object);
        Either<UserError, ? extends A> createStringEntity(String s);
        Either<UserError, ? extends A> createFileEntity(String path);
    }

    interface IRequestExecutor<Req, Res> {
        CheckedFunction1<Req, Res> getRequestExecutor();
        Either<UserError, HttpResponse> turnResponse(Res response);
    }

    interface IClient<E, R> extends
        IRequestFactory<R>,
        IEntityFactory<E>,
        IRequestExecutor<R> {
    }

}
