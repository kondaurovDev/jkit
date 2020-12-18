package jkit.db;

import io.vavr.control.Try;
import jkit.core.ext.IOExt;
import jkit.core.ext.TimeExt;
import jkit.core.ext.TryExt;
import org.h2.tools.Console;

import lombok.*;

public interface DbMain {

    static void main(String[] args) {
        IOExt.out(String.format("%s :)", jkit.core.Main.hello()));
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
