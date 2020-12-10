package jkit.http_client.context;

import io.vavr.CheckedFunction0;
import jkit.core.ext.StreamExt;
import jkit.core.ext.StringExt;
import jkit.http_client.Payload;
import lombok.val;

import java.io.File;
import java.io.FileInputStream;

public interface IPayload {

    default CheckedFunction0<Payload> createStringPayload(String input) {
        return () -> {
            val bytes = StringExt.getBytes(input);
            return Payload.of(
                StreamExt.fromBytes(bytes),
                bytes.length
            );
        };
    }

    default CheckedFunction0<Payload> createFilePayload(File file) {
        return () -> Payload.of(
            new FileInputStream(file),
            file.length()
        );
    }

}
