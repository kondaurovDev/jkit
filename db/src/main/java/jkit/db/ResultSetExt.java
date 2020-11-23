package jkit.db;

import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.ext.ListExt;
import jkit.core.ext.StringExt;
import jkit.core.ext.TryExt;
import jkit.core.model.UserError;
import jkit.db.model.DbColumn;
import jkit.db.model.DbColumnMeta;
import jkit.db.model.TableInfo;
import org.h2.jdbc.JdbcClob;

import java.sql.ResultSet;

import lombok.*;

public interface ResultSetExt {

    static Object getColumnValue(Object value) {
        if (value instanceof JdbcClob) {
            var res = TryExt
                .get(((JdbcClob)value)::getAsciiStream, "clob type to string")
                .map(StringExt::fromInputStream);

            return res.getOrElseThrow(UserError::toError);
        } else if (value instanceof Enum) {
            return ((Enum<?>)value).name();
        }

        return value;
    }

    static Either<UserError, List<Tuple2<DbColumn, Object>>> extractRow(
        ResultSet rs,
        TableInfo<?> tableInfo,
        Function2<DbColumn, Object, Object> transformKey
    ) {

        return ListExt.applyToEach(
            tableInfo.getColumnMap().values(),
            c -> TryExt.get(
                () -> {
                    val o = rs.getObject(c.getColumnName());
                    return transformKey.apply(c, getColumnValue(o));
                },
                "extract row value and transform"
            ).map(o -> Tuple.of(c, o)),
            "extract table row"
        )
        .mapLeft(e -> e.withError("extract row from result set"));

    }

    static Either<UserError, List<DbColumnMeta>> getColumns(ResultSet rs) {
        return TryExt
                .get(() -> {
                    val rsmd = rs.getMetaData();
                    val count = rsmd.getColumnCount();

                    return List.range(0, count)
                        .map(i -> TryExt.getOrThrow(
                            () -> new DbColumnMeta(
                                rsmd.getColumnLabel(i + 1),
                                rsmd.getColumnType(i + 1),
                                i == count
                            ), "columnInfo"
                        ));
                }, "get columns info");
    }

}

