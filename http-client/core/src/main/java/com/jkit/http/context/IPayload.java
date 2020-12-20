package com.jkit.http.context;

import io.vavr.CheckedFunction0;
import com.jkit.core.JKitData;
import com.jkit.core.ext.StringExt;
import com.jkit.http.Payload;

import java.nio.file.Files;
import java.nio.file.Paths;

public interface IPayload {

    JKitData.IObjMapper<?> getObjMapper();

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
                getObjMapper()
                    .serialize(input)
                    .getOrElseThrow(e -> (Error)e)
            ),
            "application/json"
        );
    }

}
