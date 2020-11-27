package jkit.entry;

import io.vavr.collection.HashMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import lombok.*;

class CommandDefTest implements Deps {

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
            Param.num, 1,
            Param.name, "alex"
        ));

        val b = 1;
    }

}