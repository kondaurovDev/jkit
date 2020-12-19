package ru.ak.jkit.jkit_core;

import ru.ak.jkit.jkit_core.ext.IOExt;

public interface Main {

    static void main(String[] args) {
        IOExt.out(hello());
    }

    static String hello() {
        return "Hello from core module!!!";
    }

}
