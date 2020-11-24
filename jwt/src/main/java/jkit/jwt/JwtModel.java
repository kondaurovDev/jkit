package jkit.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Either;
import jkit.core.ext.TimeExt;
import jkit.core.model.UserError;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JwtModel<A> {

    Class<A> aClass;
    JwtHMAC jwtHMAC;
    String issuer;
    String claimName;

    public Either<UserError, String> sign(A obj) {

        return getJwtHMAC().getObjMapper().serialize(obj)
            .flatMap(map ->
                jwtHMAC.sign(builder ->
                    builder
                        .withIssuer(issuer)
                        .withExpiresAt(TimeExt.getCurrent().plusDays(3).toDate())
                        .withClaim(claimName, map)
                )
            );

    }

    public Either<UserError, A> decode(String token) {

        return getJwtHMAC()
            .verify(token)
            .map(t -> t.getClaim(claimName).as(JsonNode.class))
            .flatMap(jsonNode -> getJwtHMAC().getObjMapper().deserialize(jsonNode, aClass));

    }

}
