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
        EntryGlobal.$.setObjectMapper(
            ObjectMapperExt.of(JacksonModule.createJsonMapper(), Validator.of())
        );
    }

    @Test
    void processParams() {

        val a = Def.test.processParams(
            HashMap.of("name", true)
        );

        assertTrue(a.isRight());

    }

    @Test
    void createReadyCommand() {

        val a = Def.test.createReadyCommand(HashMap.of(
            Param.num.getName(), 1,
            Param.name.getName(), "alex"
        ));

        val b = 1;
    }

}