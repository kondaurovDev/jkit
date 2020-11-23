package jkit.http;

import jkit.jackson.JacksonMain;
import jkit.validate.ValidatorImpl;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientImplTest {

    JacksonMain jacksonMain = JacksonMain.create(ValidatorImpl.of());

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