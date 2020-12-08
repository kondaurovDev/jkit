package jkit.http_client_apache;

import jkit.http_client_core.JKitHttpClient;
import jkit.jackson.JacksonMain;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.Validator;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;

public interface Deps {

    JacksonMain<ObjectMapperExt> jacksonMain = JacksonMain.create(Validator.of());

    JKitHttpClient.IClient<HttpEntity, HttpUriRequest> httpClient =
        HttpClientImpl.createDefault(
            jacksonMain.getJson()
        );

}
