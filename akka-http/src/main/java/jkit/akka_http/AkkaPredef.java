package jkit.akka_http;

import akka.http.javadsl.model.headers.CacheControl;
import akka.http.javadsl.model.headers.CacheDirectives;

public interface AkkaPredef {

    CacheControl noCacheControl =
        CacheControl.create(CacheDirectives.NO_CACHE);

}
