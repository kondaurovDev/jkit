package jkit.http_client_core;

import io.vavr.CheckedFunction1;
import io.vavr.Function1;
import io.vavr.control.Either;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;

public interface JKitHttpClient {

    interface IEntityFactory<A> {
        Either<UserError, ? extends A> createJsonEntity(Object object);
        Either<UserError, ? extends A> createStringEntity(String s);
        Either<UserError, ? extends A> createFileEntity(String path);
    }

    interface IRequestExecutor<Req, Res> {
        CheckedFunction1<Req, Res> getRequestExecutor();

        Either<UserError, HttpResponse> castResponse(Res response);
        Either<UserError, Req> castRequest(HttpRequest request);

        default Either<UserError, HttpResponse> execute(
            Function1<HttpRequest.HttpRequestBuilder, HttpRequest.HttpRequestBuilder> builder
        ) {
            return execute(
                builder.apply(HttpRequest.builder()).build()
            );
        }

        default Either<UserError, HttpResponse> execute(HttpRequest request) {

            return TryExt.get(() ->
                getRequestExecutor().apply(castRequest(request)),
                "execute http request"
            ).flatMap(this::castResponse);


        }

    }

    interface IClient<Req, Res> extends
        IRequestExecutor<Req, Res> {
    }

}
