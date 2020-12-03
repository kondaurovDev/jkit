package jkit.entry;

import jkit.core.ext.IOExt;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StreamTest {

    @Test
    void testOne() {

        val userLog = new UserLog();
        val b = Flux.from(userLog);

        b.subscribe(IOExt::out);

        userLog.add("Hey!");
        userLog.add("Alex!");

        userLog.end();

        userLog.add("Again");

        val asd = 1;

    }


}
