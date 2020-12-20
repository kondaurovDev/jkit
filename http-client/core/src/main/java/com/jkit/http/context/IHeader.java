package com.jkit.http.context;

import com.jkit.core.ext.StringExt;
import com.jkit.core.model.Pair;

public interface IHeader {

    String headerAuthorization = "Authorization";

    default Pair<String, String> authHeader(String v) {
        return customHeader(
            headerAuthorization,
            v
        );
    }

    default Pair<String, String> headerBearer(String t) {
        return authHeader("Bearer ".concat(t));
    }

    default Pair<String, String> headerBasicAuth(String user, String passwd) {

        String result = user.concat(":").concat(passwd);
        result = StringExt.encodeBase64(result);

        return authHeader("Basic ".concat(result));
    }

    default Pair<String, String> customHeader(String key, String value) {
        return Pair.of(
            key,
            value
        );
    }
}
