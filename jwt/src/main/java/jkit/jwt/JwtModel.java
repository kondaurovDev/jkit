package jkit.jwt;

import io.vavr.control.Either;
import jkit.core.ext.TimeExt;
import jkit.core.model.UserError;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JwtModel<A> {

    Class<A> aClass;
    JwtHMAC jwtHMAC;
    String issuer;
    String claimName;

    public Either<UserError, String> sign(A obj) {

        return getJwtHMAC().getObjMapper().objToMap(obj)
            .flatMap(map ->
                jwtHMAC.sign(builder ->
                    builder
                        .withIssuer(issuer)
//                        .withExpiresAt(TimeExt.getCurrent().plusDays(3).toDate())
                        .withClaim(claimName, map)
                )
            );

    }

    public Either<UserError, A> decode(String token) {

        return getJwtHMAC()
            .verify(token)
            .flatMap(t -> {
                val claim = t.getClaim(claimName);
                if (claim.isNull()) return Either.left(UserError.create("Unknown claim"));
                return Either.right(claim);
            })
            .flatMap(claim -> getJwtHMAC().getObjMapper().deserialize(claim.asMap(), aClass));

    }

}
