package com.jkit.http;

import com.jkit.http.client.IRequestExecutor;

public interface JKitHttpClient<Req, Res> extends
    IRequestExecutor<Req, Res> {
}
