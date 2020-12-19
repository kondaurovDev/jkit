package ru.ak.jkit.jkit_core;

public interface CorePredef {

    String cookie = "cookie";

    enum DataFormat {
        JSON,
        YAML
    }

    enum ResponseType {
        STRICT,
        STREAM
    }

}
