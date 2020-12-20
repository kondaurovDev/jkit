package com.jkit.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.vavr.Function1;
import io.vavr.control.Try;
import com.jkit.core.ext.TryExt;
import com.jkit.core.JKitData;
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

    public Try<String> sign(
        Function1<JWTCreator.Builder, JWTCreator.Builder> create
    ) {
        val builder = JWT.create();
        return TryExt
            .get(() -> create.apply(builder).sign(getAlgorithm()), "sign jwt");
    }

    public Try<DecodedJWT> verify(String token, String error) {
        return TryExt.get(
            () -> verifier.verify(token),
            String.format("verify jwt: %s", error)
        );
    }

    public Try<String> embedArrayAndSign(List<?> lst) {
        return sign(b -> b.withClaim("data", lst));
    }

    public Try<String> embedMapAndSign(Object obj) {
        return objMapper.objToMap(obj)
            .flatMap(map -> sign(b -> b.withClaim("data", map)));
    }

    public Try<Claim> verifyAndExtract(String token, String error) {
        return verify(token, error)
            .flatMap(t -> {
                val claim = t.getClaim("data");
                if (claim.isNull()) {
                    return Try.failure(new Error("No 'data' claim"));
                }
                return Try.success(claim);
            });
    }

}
