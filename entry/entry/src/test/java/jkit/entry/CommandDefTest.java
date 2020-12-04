package jkit.entry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import lombok.*;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandDefTest implements Deps {

    @Test
    void processParams() {

        val a = CmdDef.test.parseMap(
            PropMap.create()
                .param("name", "alex")
                .build()
        );

        assertTrue(a.isRight());

    }

    @Test
    void createReadyCommand() {

        val a = CmdDef.test.createReadyCommand(
            PropMap.create()
                .param(Prop.num.getName(), 1)
                .param(Prop.name.getName(), "alex")
                .build(),
            PropMap.create()
                .build()
        );

        val b = 1;
    }

}