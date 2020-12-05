package jkit.http_client;

import jkit.jackson.JacksonMain;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.Validator;

public interface Deps {

    JacksonMain<ObjectMapperExt> jacksonMain = JacksonMain.create(Validator.of());

    HttpClientImpl httpClient = HttpClientImpl.createDefault(
        jacksonMain.getJson()
    );

}
