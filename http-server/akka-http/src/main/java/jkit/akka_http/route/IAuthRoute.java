package jkit.akka_http.route;

import akka.http.javadsl.server.Route;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.vavr.Function1;
import jkit.jwt.JwtHMAC;
import lombok.*;

import java.util.Map;

public interface IAuthRoute extends ICompleteRoute {

    JwtHMAC getJwtHMAC();
    String getJwtClaimName();

    default Route withToken(
        String keyName,
        Function1<String, Route> inner
    ) {
        return withCookieOpt(keyName, cookie ->
            cookie.fold(
                () -> withOptionalParam(keyName, p -> p.fold(
                    () -> completeError("No token provided"),
                    inner
                )),
                inner
            )
        );
    }

    default Route withJWT(
        String paramName,
        Function1<DecodedJWT, Route> inner
    ) {
        return withToken(paramName, t ->
            withRight(
                getJwtHMAC().verify(t).mapLeft(e -> e.withError("Wrong auth token")),
                inner
            )
        );
    }

    default Route withUser(
        String paramName,
        Function1<Map<String, Object>, Route> inner
    ) {
        return withJWT(paramName, t -> {
            val claim = t.getClaim(getJwtClaimName());
            if (claim.isNull()) {
                return completeError("Unknown claim");
            }

            return inner
                .apply(claim.asMap());
        });
    }

}
