package com.jkit.http.client;

import io.vavr.Function2;
import io.vavr.control.Try;
import com.jkit.core.ext.TryExt;
import com.jkit.http.HttpRequest;
import com.jkit.http.HttpResponse;
import com.jkit.http.context.IContext;

public interface IRequestExecutor<Req, Res> {

    IContext getContext();

    interface ExecuteRequest<Req, Res> {
        Try<Res> execute(Req req);
    }

    ExecuteRequest<Req, Res> getRequestExecutor();

    Try<HttpResponse> castResponse(Res response);
    Try<Req> castRequest(HttpRequest request);

    default Try<HttpResponse> execute(
        Function2<HttpRequest.HttpRequestBuilder, IContext, HttpRequest.HttpRequestBuilder> builder
    ) {
        return execute(
            builder.apply(HttpRequest.builder(), getContext()).build()
        );
    }

    default Try<HttpResponse> execute(HttpRequest request) {

        return TryExt.get(() ->
            castRequest(request)
                .flatMap(r -> getRequestExecutor().execute(r)),
            "execute http request"
        ).flatMap(r -> r.flatMap(this::castResponse));

    }

}
