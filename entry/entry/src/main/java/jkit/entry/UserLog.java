package jkit.entry;

import jkit.core.iface.Entry;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.reactivestreams.Publisher;

public interface UserLog  {

//    UserLogStub stub = new UserLogStub(
//
//    );

    @Value
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class UserLogStub implements Entry.IUserLog {

        Publisher<String> publisher;

        public void end() { }

        public void add(String msg) {}

        public String getLogs() {
            return "";
        }
    }

}
