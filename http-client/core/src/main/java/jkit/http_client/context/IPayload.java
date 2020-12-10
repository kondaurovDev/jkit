package jkit.http_client.context;

import io.vavr.CheckedFunction0;
import jkit.core.ext.StreamExt;

import java.io.InputStream;

public interface IPayload {

    default CheckedFunction0<InputStream> createStringPayload(String input) {
        return StreamExt.fromString(input);
    }

    default CheckedFunction0<InputStream> createFilePayload(String file) {
        return StreamExt.fromFile(file);
    }

}
