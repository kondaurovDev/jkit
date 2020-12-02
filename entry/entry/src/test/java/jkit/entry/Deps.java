package jkit.entry;

import io.vavr.control.Either;
import jkit.core.ext.TryExt;
import jkit.jackson.JacksonModule;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.Validator;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.val;
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

    interface Def {
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
        commandMap.create(Def.test, u -> true, e -> Either.right("Hey"));
        return commandMap;
    });


}
