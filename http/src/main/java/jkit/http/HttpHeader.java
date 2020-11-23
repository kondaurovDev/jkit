package jkit.http;

import jkit.core.ext.StringExt;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;

public interface HttpHeader {

    static Header auth(String v) {
        return new BasicHeader(
            HttpHeaders.AUTHORIZATION,
            v
        );
    }

    static Header bearer(String t) {
        return auth("Bearer ".concat(t));
    }

    static Header basicAuth(String user, String passwd) {

        String result = user.concat(":").concat(passwd);
        result = StringExt.encodeBase64(result);

        return auth("Basic ".concat(result));
    }

    static Header custom(String key, String value) {
        return new BasicHeader(key, value);
    }
}
