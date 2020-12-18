package jkit.db;

import io.vavr.Function1;
import io.vavr.collection.*;
import io.vavr.control.Option;
import io.vavr.control.Try;
import jkit.core.ext.ListExt;
import jkit.db.model.DbColumn;
import jkit.db.model.TableInfo;
import jkit.core.ext.TryExt;
import jkit.db.model.IDb;

import java.sql.PreparedStatement;
import java.util.Arrays;

import jkit.db.sql.SelectExpr;
import lombok.*;

public interface IDbTable<M> extends ICreateExpr {

    DbWrapper getDbWrapper();

    TableInfo<M> getTableInfo();

    default Object transformFieldValue(DbColumn field, Object value) {
        return value;
    }

    default void setStatementParams(
        List<? extends IDb.IDbColumnWithValue> params,
        PreparedStatement s
    ) {
        params.zipWithIndex()
            .forEach(t -> TryExt
                .getOrThrow(() -> {
                    val v = t._1.getJdbcValue();
                    s.setObject(t._2 + 1, v);
                    return null;
                }, "set object")
            );
    }

    default Try<Integer> executeOneWithCheck(
        ISql.ReadyExpr readyExpr
    ) {
        return executeOne(readyExpr).flatMap(i -> {
            if (i == 0) {
                return Try.failure(new Error("Not saved"));
            } else {
                return Try.success(i);
            }
        });
    }

    default Try<Integer> executeOne(
        ISql.ReadyExpr readyExpr
    ) {
        return getDbWrapper().update(
            readyExpr.getSql(),
            s -> Try.run(() -> setStatementParams(readyExpr.getValues(), s))
        );
    }

    default Try<List<Integer>> executeMany(
        List<ISql.ReadyExpr> list
    ) {

        return list.headOption().fold(
            () -> Try.success(List.empty()),
            readyExpr ->
                executeMany(readyExpr.getSql(), list.map(ISql.ReadyExpr::getValues))
        );
    }

    default Try<List<Integer>> executeMany(
        String sql,
        List<List<? extends IDb.IDbColumnWithValue>> records
    ) {

        return getDbWrapper().batchUpdate(
            sql,
            s ->
                ListExt.applyToEach(
                    records,
                    r -> Try.of(() -> {
                        setStatementParams(r, s);
                        s.addBatch();
                        return null;
                    }),
                    "add batch",
                    true
                )
        );

    }

    default Try<Integer> delete(
        List<? extends IDb.IDbColumnCondition> where
    ) {
        String sql = "DELETE FROM "
            .concat(getTableInfo().getTableName());

        sql = ISql.joinWhereStmt(sql, where.toJavaList());

        return getDbWrapper().update(
            sql,
            s -> Try.run(() ->
                setStatementParams(where, s)
            )
        );
    }

    default Try<List<M>> getList(
        IDb.IDbColumnCondition... where
    ) {
        return getList(
            builder -> builder.where(Arrays.asList(where))
        );
    }

    default Try<List<M>> getList(
        Function1<SelectExpr.SelectExprBuilder, SelectExpr.SelectExprBuilder> inner
    ) {

        val readyExpr = inner
            .apply(SelectExpr.builder())
            .build()
            .createExpr(getTableInfo().getTableName());

        return getDbWrapper().executeSelect(
            getTableInfo(),
            readyExpr.getSql(),
            s -> Try.run(() ->
                setStatementParams(readyExpr.getValues(), s)
            ),
            this::transformFieldValue
        );

    }

    default Try<Option<M>> getOneOpt(
        ISql.WhereValue... where
    ) {
        return getList(select -> select.where(Arrays.asList(where)))
            .flatMap(v -> TryExt.assertStmt(
                v,
                x -> x.length() <= 1,
                "Expected one row but got many in " + getTableInfo().getTableName()
            ))
            .map(Traversable::headOption);
    }

    default Try<M> getOne(
        ISql.WhereValue... where
    ) {
        return getOneOpt(where)
            .flatMap(opt -> opt
                .toTry(() -> new Error("Row not found in " + getTableInfo().getTableName()))
            );
    }

}
