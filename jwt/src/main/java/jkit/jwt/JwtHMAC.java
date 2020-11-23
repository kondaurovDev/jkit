package jkit.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.Function1;
import io.vavr.control.Either;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import lombok.*;

import java.util.List;

import static jkit.core.CoreDefault.json;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JwtHMAC {

    Algorithm algorithm;
    JWTVerifier verifier;

    public static JwtHMAC create(
        String secretPhrase
    ) {
        val alg = Algorithm.HMAC256(secretPhrase);
        val verifier = JWT.require(alg)
            .build();
        return JwtHMAC.of(
            alg,
            verifier
        );
    }

    public Either<UserError, String> sign(
        Function1<JWTCreator.Builder, JWTCreator.Builder> create
    ) {
        var builder = JWT.create();
        return TryExt
            .get(() -> create.apply(builder).sign(getAlgorithm()), "sign jwt");
    }

    public Either<UserError, DecodedJWT> verify(String token) {
        return TryExt.get(
            () -> verifier.verify(token),
            "verify jwt"
        );
    }

    public Either<UserError, String> embedArrayAndSign(List<?> lst) {
        return sign(b -> b.withClaim("data", lst));
    }

    public Either<UserError, String> embedMapAndSign(Object obj) {
        return json.nodeToMap(obj)
            .flatMap(map -> sign(b -> b.withClaim("data", map.toJavaMap())));
    }

    public Either<UserError, JsonNode> verifyAndExtract(String token) {
        return verify(token)
            .flatMap(t -> {
                var claim = t.getClaim("data");
                if (claim.isNull()) {
                    return Either.left(UserError.create("No 'data' claim"));
                }
                return Either.right(claim.as(JsonNode.class));
            });
    }

}
