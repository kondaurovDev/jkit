package jkit.entry;

import jkit.core.ext.IOExt;
import lombok.val;
import org.junit.jupiter.api.*;
import reactor.core.publisher.Flux;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandMapTest implements Deps {

    @Test
    void executeTestCommand() {

        val actual = CmdDef.test.createReadyCommand(
            PropMap.create()
                .param("name", "Alex")
                .build(),
            PropMap.create()
                .build()
        );

        assertTrue(actual.isRight());

        val readyCommand = actual.get();

        val flux = Flux.from(readyCommand.getMethodContext().getUserLog().getPublisher());

        flux.subscribe(IOExt::out);

        val res = commandMap.execute(readyCommand);

        assertTrue(res.isRight());

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
