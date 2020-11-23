package jkit.core.ext;

import io.vavr.control.Either;
import io.vavr.control.Option;
import jkit.core.model.UserError;

public interface OptionExt {

    static <A> Either<UserError, A> get(Option<A> opt, String name) {
        return opt.toEither(UserError.create("Missing value: " + name));
    }

    static Either<UserError, Integer> parseInt(String s) {
        return TryExt.get(() -> Integer.parseInt(s), "to int");
    }

}
