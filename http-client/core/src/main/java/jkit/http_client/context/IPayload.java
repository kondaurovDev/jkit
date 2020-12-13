package jkit.http_client.context;

import io.vavr.CheckedFunction0;
import jkit.core.JKitData;
import jkit.core.ext.StringExt;
import jkit.core.model.UserError;
import jkit.http_client.Payload;

import java.nio.file.Files;
import java.nio.file.Paths;

public interface IPayload {

    JKitData.IObjMapperMain<?, ? extends JKitData.IObjMapper<?>> getObjMapperMain();

    default CheckedFunction0<Payload> createStringPayload(String input) {
        return () -> Payload.of(
            StringExt.getBytes(input),
            "plain/text"
        );
    }

    default CheckedFunction0<Payload> createFilePayload(String filePath) {
        return () -> Payload.of(
            Files.readAllBytes(Paths.get(filePath)),
            "application/octet-stream"
        );
    }

    default CheckedFunction0<Payload> createJsonPayload(Object input) {
        return () -> Payload.of(
            StringExt.getBytes(
                getObjMapperMain().getJson()
                    .serialize(input)
                    .getOrElseThrow(UserError::toError)
            ),
            "application/json"
        );
    }

}
