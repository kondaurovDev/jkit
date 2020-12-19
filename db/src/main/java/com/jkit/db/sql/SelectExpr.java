package com.jkit.db.sql;

import io.vavr.collection.List;
import com.jkit.db.model.IDb;

import com.jkit.db.ISql;
import lombok.*;

import java.util.stream.Collectors;

@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SelectExpr {

    @Singular("field")
    java.util.List<? extends IDb.IDbColumn> fields;
    @Singular("where")
    java.util.List<? extends IDb.IDbColumnCondition> where;

    public ISql.ReadyExpr createExpr(String tableName) {

        var columnsSql = "*";

        if (!fields.isEmpty()) {
            columnsSql = fields
                .stream().map(IDb.IDbColumn::getColumnName)
                .collect(Collectors.joining(", "));
        }

        var sql = "SELECT " + columnsSql + " FROM " + tableName;

        sql = ISql.joinWhereStmt(sql, where);

        return ISql.ReadyExpr.create(
            sql,
            List.ofAll(where)
        );
    }

}
