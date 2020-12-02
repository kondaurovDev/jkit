package jkit.db;

import jkit.jackson.JacksonMain;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.Validator;

public interface Deps {

    JacksonMain<ObjectMapperExt> jackson = JacksonMain.create(Validator.of());

    DbModule dbModule = DbModule.create(
        jackson.getJson(),
        "../runtime/db",
        DbFile::createJdbcDataSource
    );

}
