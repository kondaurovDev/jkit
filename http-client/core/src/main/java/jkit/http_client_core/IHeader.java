package jkit.http_client_core;

import jkit.core.ext.StringExt;
import jkit.core.model.Pair;

public interface IHeader {

    String headerAuthorization = "Authorization";
    String headerBearer = "Bearer";

    default Pair<String, String> authHeader(String v) {
        return Pair.of(
            headerAuthorization,
            v
        );
    }

    default Pair<String, String> headerBearer(String t) {
        return authHeader(headerBearer.concat(t));
    }

    default Pair<String, String> headerBasicAuth(String user, String passwd) {

        String result = user.concat(":").concat(passwd);
        result = StringExt.encodeBase64(result);

        return authHeader(headerBearer.concat(result));
    }

    default Pair<String, String> customHeader(String key, String value) {
        return Pair.of(
            key,
            value
        );
    }
}
