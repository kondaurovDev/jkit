package com.jkit.db;

import io.vavr.Function1;
import io.vavr.collection.*;
import io.vavr.control.Try;
import com.jkit.db.model.IDb;
import com.jkit.db.model.TableInfo;
import lombok.*;

import java.util.stream.Collectors;

public interface ISql {

    static String getWhereSql(
        String junction,
        java.util.List<? extends IDb.IDbColumnCondition> conditions
    ) {

        if (conditions.isEmpty()) {
            return "";
        }

        val join = String.format(" %s ", junction);

        val where = conditions
            .stream()
            .map(IDb.IDbColumnCondition::getSql)
            .collect(Collectors.joining(join));

        return String.format(" WHERE %s ", where);
    }

    static String joinWhereStmt(
        String sql,
        java.util.List<? extends IDb.IDbColumnCondition> where
    ) {

        return sql
            .concat(getWhereSql("AND", where));

    }

    static <A> List<String> getColumnNames(Iterable<A> rowFields, Function1<A, String> get) {
        return List.ofAll(rowFields)
            .map(get);
    }

    static List<String> getColumnNames(Iterable<? extends IDb.IDbColumn> rowFields) {
        return getColumnNames(rowFields, IDb.IDbColumn::getColumnName);
    }

    static List<String> getColumnNamesFromHolder(Iterable<? extends IDb.IDbColumnHolder> rowFields) {
        return getColumnNames(rowFields, f -> f.getColumn().getColumnName());
    }

    interface IReadyExprFactory {
        Try<ReadyExpr> getReadyExpr(TableInfo<?> tableInfo);
    }

    @Value(staticConstructor = "create")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class ReadyExpr {
        String sql;
        List<? extends IDb.IDbColumnWithValue> values;
    }

    @Value
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class WhereValue implements IDb.IDbColumnCondition {

        IDb.IDbColumn column;
        String sql;
        Object value;

        public static WhereValue createEquals(
            IDb.IDbColumn column,
            String operator,
            Object value
        ) {
            String sql = String.format(
                "%s %s ?",
                column.getColumnName(),
                operator
            );
            return new ISql.WhereValue(
                column,
                sql,
                value
            );
        }

        public static WhereValue createIn(
            IDb.IDbColumn column,
            Object value
        ) {
            String sql = String.format(
                "array_contains(?, %s)", column.getColumnName()
            );

            return new ISql.WhereValue(
                column,
                sql,
                value
            );
        }



    }



}
