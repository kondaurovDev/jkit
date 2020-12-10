package jkit.http_client.client;

import io.vavr.Function2;
import io.vavr.control.Either;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import jkit.http_client.HttpRequest;
import jkit.http_client.HttpResponse;
import jkit.http_client.context.Context;

public interface IRequestExecutor<Req, Res> {

    Context context = new Context() {};

    interface ExecuteRequest<Req, Res> {
        Either<UserError, Res> execute(Req req);
    }

    ExecuteRequest<Req, Res> getRequestExecutor();

    Either<UserError, HttpResponse> castResponse(Res response);
    Either<UserError, Req> castRequest(HttpRequest request);

    default Either<UserError, HttpResponse> execute(
        Function2<HttpRequest.HttpRequestBuilder, Context, HttpRequest.HttpRequestBuilder> builder
    ) {
        return execute(
            builder.apply(HttpRequest.builder(), context).build()
        );
    }

    default Either<UserError, HttpResponse> execute(HttpRequest request) {

        return TryExt.get(() ->
            castRequest(request)
                .flatMap(r -> getRequestExecutor().execute(r)),
            "execute http request"
        ).flatMap(r -> r.flatMap(this::castResponse));

    }

}
