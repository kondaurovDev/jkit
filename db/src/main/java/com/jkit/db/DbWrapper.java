package com.jkit.db;

import io.vavr.*;
import io.vavr.collection.List;
import io.vavr.control.Try;
import jkit.core.JKitData;
import jkit.core.ext.*;
import com.jkit.db.model.DbColumn;
import com.jkit.db.model.TableInfo;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@lombok.Value(staticConstructor = "of")
public class DbWrapper {

    JKitData.IObjMapper<?> objectMapperExt;
    DataSource ds;

    private static final Logger logger = IOExt.createLogger(DbWrapper.class);

    public static <A> Try<A> withStatement(
        CheckedFunction0<Connection> getConn,
        String query,
        CheckedFunction1<PreparedStatement, A> handler
    ) {

        logger.debug(String.format("Executing sql: %s", query));

        try(
            Connection conn = getConn.apply();
            PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            return Try.of(() -> handler.apply(stmt));
        } catch (Throwable e) {
            IOExt.log(l -> l.error("Db Error", e));
            return Try.failure(new Error("Can't execute sql query", e));
        }
    }

    public <A> Try<A> withStatement(
        String query,
        CheckedFunction1<PreparedStatement, A> handler
    ) {

        return DbWrapper.withStatement(
            this.ds::getConnection,
            query,
            handler
        );

    }

    <R> Try<List<R>> executeSelect(
        TableInfo<R> tableInfo,
        String query,
        Function1<PreparedStatement, Try<Void>> prepare,
        Function2<DbColumn, Object, Object> transformKey
    ) {

        return withStatement(query, stmt -> {
            prepare.apply(stmt);
            ResultSet rs = stmt.executeQuery();
            var result = new ArrayList<R>();
            Throwable error = null;

            while (TryExt.get(rs::next, "").contains(true) && error == null) {

                val node = ResultSetExt
                    .extractRow(rs, tableInfo, transformKey)
                    .map(lst -> lst.toMap(t -> t.map1(DbColumn::getColumnName)))
                    .flatMap(map -> objectMapperExt.convert(map, tableInfo.getModelClass()));

                if (node.isFailure()) {
                    error = node.getCause();
                } else {
                    result.add(node.get());
                }

            }

            if (error == null) {
                return Try.success(List.ofAll(result));
            } else {
                return Try.<List<R>>failure(new Error("Can't execute select query", error.getCause()));
            }
        }).flatMap(r -> r);

    }

    public Try<Integer> update(
        String query,
        Function1<PreparedStatement, Try<Void>> handlerSet
    ) {

        return withStatement(query, stmt -> {
            handlerSet.apply(stmt);
            return stmt.executeUpdate();
        });

    }

    Try<List<Integer>> batchUpdate(
        String query,
        Function1<PreparedStatement, Try<?>> handlerSet
    ) {

        return withStatement(query, stmt -> {
            handlerSet.apply(stmt);
            var res = stmt.executeBatch();
            return List.ofAll(res);
        });

    }

}
