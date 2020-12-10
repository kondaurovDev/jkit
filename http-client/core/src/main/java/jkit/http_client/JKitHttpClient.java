package jkit.http_client;

import jkit.http_client.client.IRequestExecutor;

public interface JKitHttpClient<Req, Res> extends
    IRequestExecutor<Req, Res> {

}
