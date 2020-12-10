package jkit.http_client;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.InputStream;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Payload {
    InputStream inputStream;
    long size;
}
