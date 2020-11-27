package jkit.entry;

import lombok.*;

public interface CommandError {

    @Value(staticConstructor = "of")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
    class ParamError extends Error {
        CommandParam<?> param;
        String error;
    }

}
