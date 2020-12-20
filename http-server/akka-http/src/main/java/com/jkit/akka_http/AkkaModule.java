package com.jkit.akka_http;

import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.SystemMaterializer;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value
public class AkkaModule {

    ActorSystem actorSystem;
    Materializer materializer;

    public static AkkaModule create(
        String systemName
    ) {
        val actorSystem = ActorSystem.create(systemName);
        return new AkkaModule(
            actorSystem,
            SystemMaterializer.get(actorSystem).materializer()
        );
    }

}
