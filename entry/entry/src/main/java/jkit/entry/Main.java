package jkit.entry;

import com.jkit.core.ext.IOExt;

public interface Main {

    static void main(String[] args) {
        IOExt.out(com.jkit.core.Main.hello() + "###");
        IOExt.out("Yuk");
    }

    static String hello() {
        return "Hello from server";
    }

}
