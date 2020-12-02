package jkit.entry;

import jkit.core.iface.Entry;

public interface UserLog  {

    UserLogStub stub = new UserLogStub();

    class UserLogStub implements Entry.IUserLog {
        public void end() { }

        public void add(String msg) {}

        public String getLogs() {
            return "";
        }
    }

}
