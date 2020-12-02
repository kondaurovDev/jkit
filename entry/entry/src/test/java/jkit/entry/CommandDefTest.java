package jkit.entry;

import io.vavr.collection.HashMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import lombok.*;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandDefTest implements Deps {

    @Test
    void processParams() {

        val a = Def.test.parseMap(
            PropMap.builder()
                .param("namee", true)
                .build()
        );

        assertTrue(a.isRight());

    }

    @Test
    void createReadyCommand() {

        val a = Def.test.createReadyCommand(
            PropMap.builder()
                .param(Prop.num.getName(), 1)
                .param(Prop.name.getName(), "alex")
                .build()
        );

        val b = 1;
    }

}