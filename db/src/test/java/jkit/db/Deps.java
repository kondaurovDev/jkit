package jkit.db;

import jkit.jackson.JKitJackson;
import jkit.validate.Validator;

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
