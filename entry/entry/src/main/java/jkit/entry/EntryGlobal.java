package jkit.entry;

import io.vavr.control.Either;
import jkit.core.iface.IObjMapper;
import jkit.core.model.UserError;
import lombok.*;

public interface EntryGlobal {

    Global $ = Global.create();

    @Data(staticConstructor = "create")
    class Global {

        private IObjMapper objectMapper;

        public Either<UserError, IObjMapper> getObjectMapper() {
            if (objectMapper == null)
                return Either.left(UserError.create("ObjectMapper not initialized"));

            return Either.right(objectMapper);
        }

    }

}
