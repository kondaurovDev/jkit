package com.jkit.db;

import com.jkit.jackson.JKitJackson;
import com.jkit.validate.Validator;

public interface Deps {

    JKitJackson jackson = JKitJackson.create(
        Validator.of(),
        (module, obj, factory) -> factory
            .configureObjectMapper(obj)
    );

    DbModule dbModule = DbModule.create(
        jackson,
        "../runtime/db",
        DbFile::createJdbcDataSource
    );

}
