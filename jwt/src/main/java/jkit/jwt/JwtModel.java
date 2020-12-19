package jkit.jwt;

import io.vavr.control.Try;
import lombok.*;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JwtModel<A> {

    Class<A> aClass;
    JwtHMAC jwtHMAC;
    String issuer;
    String claimName;

    public Try<String> sign(A obj) {

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

    public Try<A> decode(String token) {

        return getJwtHMAC()
            .verify(token)
            .flatMap(t -> {
                val claim = t.getClaim(claimName);
                if (claim.isNull()) return Try.failure(new Error("Unknown claim"));
                return Try.success(claim);
            })
            .flatMap(claim -> getJwtHMAC().getObjMapper().deserialize(claim.asMap(), aClass));

    }

}
