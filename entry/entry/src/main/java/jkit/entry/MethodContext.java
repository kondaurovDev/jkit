package jkit.entry;

import jkit.core.iface.Entry;
import lombok.*;

import java.util.ArrayList;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MethodContext<U> implements Entry.IMethodContext<U> {
    Entry.IParamsMap params;
    U user;
    Entry.IUserLog userLog;
    ArrayList<String> logHistory = new ArrayList<>();

    public void log(String msg) {
        this.userLog.add(msg);
        this.logHistory.add(msg);
    }

    public String getLogs() {
        return String.join("\n", this.logHistory);
    }

}
