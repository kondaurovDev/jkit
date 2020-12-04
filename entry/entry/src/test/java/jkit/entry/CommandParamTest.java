package jkit.entry;

import org.junit.jupiter.api.Test;

import lombok.*;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandParamTest implements Deps {

    @Test
    void processInput() {

        val a = CmdDef.test;

        val b = 1;

    }
}