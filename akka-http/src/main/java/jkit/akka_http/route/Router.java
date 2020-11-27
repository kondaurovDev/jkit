package jkit.akka_http.route;

import akka.http.javadsl.server.AllDirectives;

public interface Router {

    abstract class Slim extends AllDirectives implements IRouter, IPayloadRoute {}

//    abstract class Full<U> extends Slim implements ICommandRoute<U> {}

}
