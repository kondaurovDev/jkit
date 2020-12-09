package jkit.http_client_apache;

import jkit.jackson.JacksonMain;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.Validator;

public interface Deps {

    JacksonMain<ObjectMapperExt> jacksonMain = JacksonMain.create(Validator.of());

    HttpClientApache.Client httpClient =
        HttpClientApache.create(
            jacksonMain.getJson(),
            HttpClientApache.createDefaultClient()
        );

}
