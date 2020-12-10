package jkit.http_client;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Either;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import jkit.http_client.context.Context;

import java.nio.charset.StandardCharsets;

public interface JKitHttpClient {

    Context context = new Context() {};

    interface IRequestExecutor<Req, Res> {
        Function1<Req, Either<UserError, Res>> getRequestExecutor();

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
                    .flatMap(r -> getRequestExecutor().apply(r)),
                "execute http request"
            ).flatMap(r -> r.flatMap(this::castResponse));

        }

        default String readStringResponse(HttpResponse response) {
            return new String(response.getBody(), StandardCharsets.UTF_8);
        }

        default String readJsonResponse(HttpResponse response) {
            return new String(response.getBody(), StandardCharsets.UTF_8);
        }

    }

    interface IClient<Req, Res> extends
        IRequestExecutor<Req, Res> {
    }

}
