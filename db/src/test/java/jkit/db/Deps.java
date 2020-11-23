package jkit.db;

import jkit.jackson.JacksonMain;
import jkit.jackson.ObjectMapperExt;
import jkit.validate.ValidatorImpl;

public interface Deps {

    JacksonMain<ObjectMapperExt> jackson = JacksonMain.create(ValidatorImpl.of());

    DbModule dbModule = DbModule.create(
        jackson.getJson(),
        "../runtime/db",
        DbFile::createJdbcDataSource
    );

}
