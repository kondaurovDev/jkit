package jkit.entry;

import jkit.core.iface.Entry;
import lombok.*;

public interface CommandError {

    @Value(staticConstructor = "of")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
    class ParamError extends Error {
        Entry.IPropDef<?> param;
        String error;
    }

}
