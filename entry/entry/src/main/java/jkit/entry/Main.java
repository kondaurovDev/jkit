package jkit.entry;

import jkit.core.ext.IOExt;

public interface Main {

    static void main(String[] args) {
        IOExt.out(jkit.core.Main.hello() + "###");
        IOExt.out("Yuk");
    }

    static String hello() {
        return "Hello from server";
    }

}
