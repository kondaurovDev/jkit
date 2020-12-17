package jkit.entry;

import io.vavr.control.Option;
import jkit.core.ext.IOExt;
import jkit.core.model.JKitError;

public interface Main {

    static void main(String[] args) {
        IOExt.out(jkit.core.Main.hello() + "###");
        IOExt.out("Yuk");
    }

    static String hello() {
        JKitError.of("err", Option.none());
        return "Hello from server";
    }

}
