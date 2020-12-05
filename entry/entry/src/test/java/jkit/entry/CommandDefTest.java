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
                .prop("name", "alex")
                .build()
        );

        assertTrue(a.isRight());

    }

    @Test
    void createReadyCommand() {

        val a = CmdDef.test.createReadyCommand(
            PropMap.create()
                .prop(Prop.num.getName(), 1)
                .prop(Prop.name.getName(), "alex")
                .build(),
            PropMap.create()
                .build()
        );

        val b = 1;
    }

}