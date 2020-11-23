package jkit.db;

import io.vavr.*;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jkit.core.ext.*;
import jkit.core.iface.IObjMapper;
import jkit.core.model.UserError;
import jkit.db.model.DbColumn;
import jkit.db.model.TableInfo;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@lombok.Value(staticConstructor = "of")
public class DbWrapper {

    IObjMapper objectMapperExt;
    DataSource ds;

    private static final Logger logger = IOExt.createLogger(DbWrapper.class);

    public static <A> Either<UserError, A> withStatement(
        CheckedFunction0<Connection> getConn,
        String query,
        CheckedFunction1<PreparedStatement, A> handler
    ) {

        logger.debug(String.format("Executing sql: %s", query));

        try(
            Connection conn = getConn.apply();
            PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            return Either.right(handler.apply(stmt));
        } catch (Throwable e) {
            IOExt.log(l -> l.error("Db Error", e));
            return Either.left(UserError
                .create("Can't execute sql query", e)
            );
        }
    }

    public <A> Either<UserError, A> withStatement(
        String query,
        CheckedFunction1<PreparedStatement, A> handler
    ) {

        return DbWrapper.withStatement(
            this.ds::getConnection,
            query,
            handler
        );

    }

    <R> Either<UserError, List<R>> executeSelect(
        TableInfo<R> tableInfo,
        String query,
        Function1<PreparedStatement, Try<Void>> prepare,
        Function2<DbColumn, Object, Object> transformKey
    ) {

        return withStatement(query, stmt -> {
            prepare.apply(stmt);
            ResultSet rs = stmt.executeQuery();
            var result = new ArrayList<R>();
            UserError error = null;

            while (TryExt.get(rs::next, "").contains(true) && error == null) {

                val node = ResultSetExt
                    .extractRow(rs, tableInfo, transformKey)
                    .map(lst -> lst.toMap(t -> t.map1(DbColumn::getColumnName)))
                    .flatMap(map -> objectMapperExt.convert(map, tableInfo.getModelClass()));

                if (node.isLeft()) {
                    error = node.getLeft();
                } else {
                    result.add(node.get());
                }

            }

            if (error == null) {
                return Either.<UserError, List<R>>right(List.ofAll(result));
            } else {
                return Either.<UserError, List<R>>left(error);
            }
        }).flatMap(r -> r)
        .mapLeft(e -> e.withError("Can't execute select query"));

    }

    public Either<UserError, Integer> update(
        String query,
        Function1<PreparedStatement, Try<Void>> handlerSet
    ) {

        return withStatement(query, stmt -> {
            handlerSet.apply(stmt);
            return stmt.executeUpdate();
        });

    }

    Either<UserError, List<Integer>> batchUpdate(
        String query,
        Function1<PreparedStatement, Either<UserError, Void>> handlerSet
    ) {

        return withStatement(query, stmt -> {
            handlerSet.apply(stmt);
            var res = stmt.executeBatch();
            return List.ofAll(res);
        });

    }

}
