package com.jkit.http;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Payload {
    byte[] body;
    String contentType;
}
