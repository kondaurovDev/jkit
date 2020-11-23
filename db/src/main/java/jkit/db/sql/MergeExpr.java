package jkit.db.sql;

import io.vavr.collection.List;
import jkit.db.model.IDb;

import jkit.db.ISql;
import lombok.*;

@Builder
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class MergeExpr {

    @Singular("field")
    java.util.List<IDb.IDbColumnWithValue> fields;
    @Singular("where")
    java.util.List<IDb.IDbColumn> where;

    public ISql.ReadyExpr createExpr(
        String tableName
    ) {
        val columnNames = ISql.getColumnNamesFromHolder(fields)
            .mkString(", ");

        val fieldPlaceholders = List.ofAll(fields)
            .map(s -> {
                var res = "?";
                if (s.getColumn().getIsJson()) {
                    res += " FORMAT JSON";
                }
                return res;
            })
            .mkString(", ");

        val sql =
            "MERGE INTO ".concat(tableName).concat(" (")
                .concat(columnNames).concat(") ")
                .concat(" key (").concat(ISql.getColumnNames(where).mkString(", ")).concat(") ")
                .concat("values (").concat(fieldPlaceholders).concat(")");

        return ISql.ReadyExpr.create(
            sql,
            List.ofAll(fields)
        );
    }

}
