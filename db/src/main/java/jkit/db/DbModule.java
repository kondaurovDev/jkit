package jkit.db;

import io.vavr.Function1;
import io.vavr.control.Either;
import jkit.core.JKitData;
import jkit.core.ext.TimeExt;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
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

    public Either<UserError, Void> runSql(
        String sql,
        String processName
    ) {
        return DbWrapper.withStatement(
            dbFile::getSingleConnection,
            sql,
            PreparedStatement::execute
        )
        .mapLeft(e -> e.withError(processName))
        .map(s -> null);
    }

    public Either<UserError, String> runMigration() {
        return TryExt.get(() -> {
            val res = flyway.migrate();
            return "migrated: " + res;
        }, "migrate");
    }

    public Either<UserError, ?> backupDb(String destFile) {
        return runSql(
            DbModule.backupSql(destFile),
            "creating db backup"
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
