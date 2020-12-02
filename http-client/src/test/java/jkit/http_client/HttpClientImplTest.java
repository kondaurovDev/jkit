package jkit.http_client;

import jkit.jackson.JacksonMain;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.Validator;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientImplTest {

    JacksonMain<ObjectMapperExt> jacksonMain = JacksonMain.create(Validator.of());

    HttpClientImpl httpClient = HttpClientImpl.createDefault(
        jacksonMain.getJson()
    );

    @Test
    void getGooglePage() {

        val a =
            httpClient
                .createGetRequest("https://google.com")
                .flatMap(httpClient::getStringResponse);

        assertTrue(a.isRight());

    }

}