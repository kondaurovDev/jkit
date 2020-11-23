package jkit.db.sql;

import jkit.core.ext.ListExt;
import jkit.db.model.IDb;
import jkit.db.ISql;
import lombok.*;

@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UpdateExpr {

    @Singular("field")
    java.util.List<? extends IDb.IDbColumnWithValue> fields;
    @Singular("where")
    java.util.List<? extends IDb.IDbColumnCondition> where;

    public ISql.ReadyExpr createExpr(String tableName) {

        val columnNames = ISql.getColumnNamesFromHolder(fields)
            .map(columnName -> columnName + " = ?")
            .mkString(", ");

        var sql =
            "UPDATE ".concat(tableName).concat(" set ")
                .concat(columnNames);

        sql = ISql.joinWhereStmt(sql, where);

        return ISql.ReadyExpr.create(
            sql,
            ListExt.merge(fields, where)
        );

    }

}
