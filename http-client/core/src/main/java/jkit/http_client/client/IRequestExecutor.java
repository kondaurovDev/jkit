package jkit.http_client.client;

import io.vavr.Function2;
import io.vavr.control.Either;
import jkit.core.ext.TryExt;
import jkit.core.model.JKitError;
import jkit.http_client.HttpRequest;
import jkit.http_client.HttpResponse;
import jkit.http_client.context.IContext;

public interface IRequestExecutor<Req, Res> {

    IContext getContext();

    interface ExecuteRequest<Req, Res> {
        Either<JKitError, Res> execute(Req req);
    }

    ExecuteRequest<Req, Res> getRequestExecutor();

    Either<JKitError, HttpResponse> castResponse(Res response);
    Either<JKitError, Req> castRequest(HttpRequest request);

    default Either<JKitError, HttpResponse> execute(
        Function2<HttpRequest.HttpRequestBuilder, IContext, HttpRequest.HttpRequestBuilder> builder
    ) {
        return execute(
            builder.apply(HttpRequest.builder(), getContext()).build()
        );
    }

    default Either<JKitError, HttpResponse> execute(HttpRequest request) {

        return TryExt.get(() ->
            castRequest(request)
                .flatMap(r -> getRequestExecutor().execute(r)),
            "execute http request"
        ).flatMap(r -> r.flatMap(this::castResponse));

    }

}
