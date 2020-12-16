package jkit.http_client_apache;

import jkit.jackson.JacksonMain;
import jkit.jackson.JKitJackson;
import jkit.validate.Validator;

public interface Deps {

    JacksonMain<JKitJackson> jacksonMain = JacksonMain.create(Validator.of());

    HttpClientApache.Client httpClient =
        HttpClientApache.create(
            jacksonMain,
            HttpClientApache.createDefaultClient()
        );

}
