package jkit.core.model;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserError {

    String errorMsg;
    ArrayList<UserError> userErrors = new ArrayList<>();
    Option<Throwable> throwable;

    public static UserError create(String errorMsg) {
        return UserError.of(errorMsg, Option.none());
    }

    public static UserError create(String errorMsg, Throwable e) {
        return UserError.of(errorMsg, Option.some(e));
    }

    public UserError withError(UserError e) {
        this.userErrors.add(e);
        return this;
    }

    public UserError withErrors(Collection<UserError> e) {
        this.userErrors.addAll(e);
        return this;
    }

    public UserError withError(String e) {
        this.userErrors.add(UserError.create(e));
        return this;
    }

    public Option<String> getThrowableString() {
        return throwable.map(e ->
            "(".concat(Option.of(e.getMessage()).getOrElse("")).concat(")")
        );
    }

    public String toString() {
        return List
            .ofAll(userErrors)
            .map(UserError::toString)
            .prepend(errorMsg + getThrowableString().getOrElse(""))
            .reverse()
            .mkString("; ");
    }

    public Error toError() {
        return new Error(toString());
    }

}
