package jkit.http_client_apache;

import com.jkit.http_client_apache.HttpClientApache;
import com.jkit.jackson.JKitJackson;
import com.jkit.validate.Validator;

public interface Deps {

    JKitJackson jackson = JKitJackson.create(
        Validator.of(),
        (a, b, c) -> c.configureObjectMapper(b)
    );

    HttpClientApache.Client httpClient =
        HttpClientApache.create(
            jacksonMain,
            HttpClientApache.createDefaultClient()
        );

}
