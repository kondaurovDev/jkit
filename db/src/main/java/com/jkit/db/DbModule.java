package com.jkit.db;

import io.vavr.Function1;
import io.vavr.control.Try;
import jkit.core.JKitData;
import jkit.core.ext.TimeExt;
import jkit.core.ext.TryExt;
import org.flywaydb.core.Flyway;

import java.sql.PreparedStatement;
import lombok.*;

import javax.sql.DataSource;

@Value(staticConstructor = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DbModule {

    DbFile dbFile;

    DbWrapper dbWrapper;

    Flyway flyway;

    public static DbModule create(
        JKitData.IObjMapper<?> objectMapper,
        String filePath,
        Function1<DbFile, DataSource> createDataSource
    ) {
        TimeExt.setUtc();
        val dbFile = DbFile.create(filePath);
        val dbWrapper =
            DbWrapper.of(objectMapper, createDataSource.apply(dbFile));
        val flyway = Flyway.configure()
            .dataSource(dbWrapper.getDs())
            .load();

        return DbModule.of(
            dbFile,
            dbWrapper,
            flyway
        );

    }

    public Try<?> runSql(
        String sql
    ) {
        return DbWrapper.withStatement(
            dbFile::getSingleConnection,
            sql,
            PreparedStatement::execute
        );
    }

    public Try<String> runMigration() {
        return TryExt.get(() -> {
            val res = flyway.migrate();
            return "migrated: " + res;
        }, "migrate");
    }

    public Try<?> backupDb(String destFile) {
        return runSql(
            DbModule.backupSql(destFile)
        );
    }

    public static String backupSql(
        String fileName
    ) {
        return String.format(
            "BACKUP to '%s'",
            fileName
        );
    }

}
