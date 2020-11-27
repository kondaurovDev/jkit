package jkit.akka_http;

import akka.actor.AbstractActor;
import akka.actor.Props;
import jkit.core.ext.IOExt;

public class SimpleActor extends AbstractActor {

    public Receive createReceive() {
        return receiveBuilder()
            .match(
                String.class,
                s -> IOExt.log(l -> l.debug("Got message: " + l))
            )
            .matchAny(o -> IOExt.log(l -> l.debug("Got unknown msg: " + o)))
            .build();
    }

    public static Props props() {
        return Props.create(SimpleActor.class, SimpleActor::new);
    }

}