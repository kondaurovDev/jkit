package jkit.entry;

import jkit.core.iface.Entry;
import lombok.val;
import org.junit.jupiter.api.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandMapTest implements Deps {

    @Test
    void executeTestCommand() {

        val actual = commandMap.execute(
            "test",
            ExecuteCmdRequest.of(
                PropMap.builder()
                    .param("name", "alex")
                    .build(),
                PropMap.builder().build(),
                Entry.ResponseType.STRICT
            )
        );

        assertTrue(actual.isRight());

    }

    @Test
    void executeTestCommandAndGetStream() {

        val s = Stream.empty();

//        val actual = commandMap.execute(
//            "test",
//            ExecuteCmdRequest.of(
//                PropMap.builder()
//                    .param("name", "alex")
//                    .build(),
//                PropMap.builder().build(),
//                Entry.ResponseType.STREAM
//            )
//        );
//
//        assertTrue(actual.isRight());

    }


}
