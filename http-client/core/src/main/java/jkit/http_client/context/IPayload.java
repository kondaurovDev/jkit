package jkit.http_client.context;

import io.vavr.CheckedFunction0;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public interface IPayload {

    default InputStream createPayload(byte[] input) {
        return new ByteArrayInputStream(input);
    }

    default CheckedFunction0<InputStream> createStringPayload(String input) {
        return () -> new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    }

//    default InputStream createFilePayload(String file) {
//        return new FileInputStream(file);
//    }

}
