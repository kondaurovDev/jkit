package jkit.http_client;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientImplTest implements Deps {

    @Test
    void getGooglePage() {

        val actual =
            httpClient
                .createGetRequest("https://google.com")
                .flatMap(httpClient::getStringResponse);

        assertTrue(actual.isRight());

    }

}