package jkit.entry;

import io.vavr.collection.HashMap;
import jkit.jackson.JacksonModule;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import lombok.*;

class CommandDefTest implements Deps {

    @BeforeAll
    static void init() {
        val mapper = ObjectMapperExt.of(JacksonModule.createJsonMapper(), Validator.of());
        EntryGlobal.$.setObjectMapper(mapper);
    }

    @Test
    void processParams() {

        val a = Def.test.parseMap(
            HashMap.of("name", true)
        );

        assertTrue(a.isRight());

    }

    @Test
    void createReadyCommand() {

        val a = Def.test.createReadyCommand(HashMap.of(
            Prop.num.getName(), 1,
            Prop.name.getName(), "alex"
        ));

        val b = 1;
    }

}