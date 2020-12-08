package jkit.http_client_core;

import io.vavr.control.Either;
import jkit.core.model.UserError;

public interface JKitHttpClient {

    interface IRequestFactory<A> {
        Either<UserError, ? extends A> createGetRequest(String uri);
        Either<UserError, ? extends A> createPostRequest(String uri);
        Either<UserError, ? extends A> createDeleteRequest(String uri);
        Either<UserError, ? extends A> createPutRequest(String uri);
    }

    interface IEntityFactory<A> {
        Either<UserError, ? extends A> createJsonEntity(Object object);
        Either<UserError, ? extends A> createStringEntity(String s);
        Either<UserError, ? extends A> createFileEntity(String path);
    }

    interface IRequestExecutor<A> {
        Either<UserError, HttpResponse<String>> getJsonResponse(A request);
        Either<UserError, HttpResponse<String>> getStringResponse(A request);
    }

    interface IClient<E, R> extends
        IRequestFactory<R>,
        IEntityFactory<E>,
        IRequestExecutor<R> {
    }

}
