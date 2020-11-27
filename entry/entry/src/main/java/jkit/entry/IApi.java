package jkit.entry;

import io.vavr.collection.Stream;
import io.vavr.control.Either;
import jkit.core.ext.*;
import jkit.core.model.UserError;
import java.util.function.Consumer;

public interface IApi {

    interface IResponse {

        default Route getResponse(
            IRouter router
        ) {
           return this.getRoute(router, e -> {});
        }

        Stream<?> getResponse(
            Consumer<CommandEvent> onSave
        );
    }

    interface Executor<U> {
        Either<UserError, ?> execute(MethodContext<U> methodContext);
    }

    interface AccessChecker<U> {
        Boolean check(MethodContext<U> ctx);
    }

    enum ResponseType {
        stream(UserLog.UserLogActor::create),
        strict(akkaModule -> UserLog.stub);

        public final Function1<AkkaModule, UserLog.IUserLog> createLog;

        public static Either<UserError, ResponseType> fromString(String name) {
            return EnumExt.getByName(name, ResponseType.class);
        }

        ResponseType(Function1<AkkaModule, UserLog.IUserLog> createLog) {
            this.createLog = createLog;
        }
    }

    enum DataFormat {
        json,
        yaml;
    }

    interface Name {
        String getName();
    }

}
