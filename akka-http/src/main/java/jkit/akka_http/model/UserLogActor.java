package jkit.akka_http.model;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.Status.Success;
import akka.http.javadsl.model.sse.ServerSentEvent;
import akka.japi.Pair;
import akka.stream.CompletionStrategy;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.Source;
import jkit.akka_http.AkkaModule;
import jkit.core.ext.IOExt;
import jkit.core.iface.Entry;
import lombok.*;

import java.util.Optional;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class UserLogActor implements Entry.IUserLog {

    private static Integer bufferSize = 100;
    private static String empty = "";

    Source<Object, ActorRef> src;
    Pair<ActorRef, Source<Object, NotUsed>> actorRefSourcePair;

    public String getLogs() {
        return empty;
    }

    public static UserLogActor create(AkkaModule akkaModule) {
        val source = createSource();
        val res =  new UserLogActor(
            source,
            source.preMaterialize(akkaModule.getMaterializer())
        );
        res.getSseSource()
            .map(e -> {
                IOExt.out(e.getData());
                return e;
            })
            .run(akkaModule.getMaterializer());
        return res;
    }

    public Source<ServerSentEvent, NotUsed> getSseSource() {
        return actorRefSourcePair.second()
            .map(r -> {
                if (r instanceof String) {
                   return ServerSentEvent.create((String)r);
                } else if (r instanceof Success) {
                   return ServerSentEvent.create("$$$end");
                } else {
                   throw new Error("Unknown sse message");
                }
            });
    }

    void tell(Object msg) {
        actorRefSourcePair.first().tell(msg, ActorRef.noSender());
    }

    public void end() {
        tell(new Success(CompletionStrategy.draining()));
    }

    public void add(String msg) {
        tell(msg);
    }

    public static Source<Object, ActorRef> createSource() {
        return Source.actorRef(
            elem -> {
                // complete stream immediately if we send it Done
                if (elem == Done.done()) {
                    return Optional.of(CompletionStrategy.immediately());
                } else  {
                    return Optional.empty();
                }
            },
            // never fail the stream because of a message
            elem -> Optional.empty(),
            bufferSize,
            OverflowStrategy.fail()
        );
    }

}