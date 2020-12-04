package jkit.entry;

import jkit.core.ext.IOExt;
import jkit.core.ext.TryExt;
import jkit.jackson.JacksonModule;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.Validator;
import lombok.*;
import org.junit.jupiter.api.BeforeAll;

public interface Deps {

    @BeforeAll
    default void init() {
        val mapper = ObjectMapperExt.of(JacksonModule.createJsonMapper(), Validator.of());
        EntryGlobal.$.setObjectMapper(mapper);
    }

    interface Prop {
        PropDef<?> name = PropDef.of("name", String.class);
        PropDef<?> flag = PropDef.of("flag", CommandFlag.class);
        PropDef<?> num = PropDef.of("num", Integer.class);
        PropDef<?> arr = PropDef.of("likes", Integer.class, true);
    }

    interface CmdDef {
        CommandDef test = CommandDef.builder()
            .name("test")
            .param(Prop.name)
            .required(Prop.name.getName())
            .build();
    }

    @Value(staticConstructor = "of")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class User {
        String name;
    }

    CommandMap commandMap = TryExt.get(() -> {
        val commandMap = CommandMap.create();
        commandMap.create(CmdDef.test, u -> true, ctx ->
            ctx
                .getParams()
                .propOpt(Prop.name)
                .map(name -> {
                    IOExt.out("starting job");
                    var res = String.format("Hello %s!", name);
                    ctx.getUserLog().add("log event 1");
                    IOExt.sleepSec(3);
                    ctx.getUserLog().add("log event 2");
                    IOExt.sleepSec(3);
                    ctx.getUserLog().add("log event 3");
                    return res;
                })
        );
        return commandMap;
    });


}
