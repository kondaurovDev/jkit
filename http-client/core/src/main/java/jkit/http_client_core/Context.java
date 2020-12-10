package jkit.http_client_core;

import jkit.core.model.Pair;

public interface Context extends IHeader {

    String contentType = "content-type";
    Pair<String, String> ctText = Pair.of(contentType, "text/plain");
    Pair<String, String> ctJson = Pair.of(contentType, "application/json");
    String methodPost = "POST";
    String methodGet = "GET";

}
