package jkit.entry;

import jkit.core.iface.Entry;
import lombok.val;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandMapTest implements Deps {

    @Test
    void testOne() {

        val actual = commandMap.execute(
            "test",
            ExecuteCmdRequest.of(
            PropMap.builder()
                .param("arr", 123)
                .build(),
            PropMap.builder().build(),
            Entry.ResponseType.STRICT
        ));

        assertTrue(actual.isRight());

    }

}
