package jkit.entry;

import io.vavr.control.Option;
import jc.core.model.UserError;

import static jc.core.CoreDefault.*;

public interface Main {

    static void main(String[] args) {
        var a = json.newObj().put("name", "Alex");
        System.out.println(jc.core.Main.hello() + "###");
        System.out.println("Yuk: " + json.toJsonString(a));
    }

    static String hello() {
        UserError.of("err", Option.none());
        return "Hello from server";
    }

}
