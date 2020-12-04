package jkit.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.vavr.Function1;
import io.vavr.control.Either;
import jkit.core.ext.TryExt;
import jkit.core.JKitData;
import jkit.core.model.UserError;
import lombok.*;

import java.util.List;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JwtHMAC {

    JKitData.IObjMapper<?> objMapper;
    Algorithm algorithm;
    JWTVerifier verifier;

    public static JwtHMAC create(
        JKitData.IObjMapper<?> objMapper,
        String secretPhrase
    ) {
        val alg = Algorithm.HMAC256(secretPhrase);
        val verifier = JWT.require(alg)
            .build();
        return JwtHMAC.of(
            objMapper,
            alg,
            verifier
        );
    }

    public Either<UserError, String> sign(
        Function1<JWTCreator.Builder, JWTCreator.Builder> create
    ) {
        val builder = JWT.create();
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
        return objMapper.objToMap(obj)
            .flatMap(map -> sign(b -> b.withClaim("data", map)));
    }

    public Either<UserError, Claim> verifyAndExtract(String token) {
        return verify(token)
            .flatMap(t -> {
                val claim = t.getClaim("data");
                if (claim.isNull()) {
                    return Either.left(UserError.create("No 'data' claim"));
                }
                return Either.right(claim);
            });
    }

}
