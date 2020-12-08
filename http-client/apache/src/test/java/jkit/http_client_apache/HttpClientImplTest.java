package jkit.http_client_apache;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientImplTest implements Deps {

    @Test
    void getGooglePage() {

        val actual =
            httpClient
                .createGetRequest("https://google.com")
                .flatMap(httpClient::getStringResponse)
                .flatMap(r -> httpClient.filterByCode(r,200));

        assertTrue(actual.isRight());

    }

}