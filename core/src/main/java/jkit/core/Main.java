package jkit.core;

import jkit.core.ext.IOExt;

public interface Main {

    static void main(String[] args) {
        IOExt.out(hello());
    }

    static String hello() {
        return "Hello from core module!!!";
    }

}
