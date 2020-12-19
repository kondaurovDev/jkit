package jkit.entry.akka_http;

import jkit.http_client.HttpClientImpl;
import jkit.jackson.JacksonMain;
import com.jkit.jackson.JKitJackson;
import com.jkit.validate.Validator;

interface Deps {

    JacksonMain<JKitJackson> jacksonMain = JacksonMain.create(
        Validator.of()
    );

    HttpClientImpl httpClient = HttpClientImpl.createDefault(
        jacksonMain.getJson()
    );
}
