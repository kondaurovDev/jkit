package jkit.entry.akka_http;

import jkit.jackson.JacksonMain;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.Validator;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    JacksonMain<ObjectMapperExt> jacksonMain = JacksonMain.create(
        Validator.of()
    );

    @Test
    void bind() {
        val router = Router.create(jacksonMain);

        val actual = Main.bind(router);

        assertTrue(actual.isRight());
    }

}