package com.jkit.db;

import io.vavr.control.Try;
import com.jkit.core.ext.*;
import org.h2.tools.Console;

import lombok.*;

public interface DbMain {

    static void main(String[] args) {
        IOExt.out(String.format("%s :)", com.jkit.core.Main.hello()));
        var res = runDbUi();
        res.get();
    }

    static Try<Void> runDbUi() {
        TimeExt.setUtc();
        return TryExt.getAndVoid(
            Console::main,
            "run db ui"
        );
    }

}
