package jkit.http_client_apache;

import jkit.core.model.Pair;
import jkit.http_client_core.HttpResponse;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientImplTest implements Deps {

    @Test
    void getGooglePage() {

        val resp =
            httpClient
                .execute(builder ->
                    builder
                        .method("get")
                        .url("https://google.com")
                        .queryParam(Pair.of("command", "run"))
                );
        
        val body = resp.map(HttpResponse::getBodyString);

        assertTrue(resp.isRight());
        assertTrue(body.isRight());

    }

}