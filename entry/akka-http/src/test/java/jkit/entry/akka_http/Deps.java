package jkit.entry.akka_http;

import jkit.http_client.HttpClientImpl;
import jkit.jackson.JacksonMain;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.Validator;

interface Deps {

    JacksonMain<ObjectMapperExt> jacksonMain = JacksonMain.create(
        Validator.of()
    );

    HttpClientImpl httpClient = HttpClientImpl.createDefault(
        jacksonMain.getJson()
    );
}
