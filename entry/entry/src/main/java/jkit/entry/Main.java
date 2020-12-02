package jkit.entry;

import io.vavr.control.Option;
import jkit.core.ext.IOExt;
import jkit.core.model.UserError;

public interface Main {

    static void main(String[] args) {
        IOExt.out(jkit.core.Main.hello() + "###");
        IOExt.out("Yuk");
    }

    static String hello() {
        UserError.of("err", Option.none());
        return "Hello from server";
    }

}
