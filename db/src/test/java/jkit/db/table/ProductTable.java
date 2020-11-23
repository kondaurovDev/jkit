package jkit.db.table;

import io.vavr.collection.List;
import io.vavr.control.Either;
import jkit.core.model.UserError;
import jkit.db.DbWrapper;
import jkit.db.IDbTable;
import jkit.db.model.DbColumn;
import jkit.db.model.TableInfo;
import lombok.*;
import org.joda.time.LocalDateTime;

public interface ProductTable {

    TableInfo<Row> tableInfo = TableInfo.create(
        "product",
        Field.all,
        Row.class
    );

    @Value(staticConstructor = "of")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class Table implements IDbTable<Row> {

        DbWrapper dbWrapper;
        TableInfo<Row> tableInfo = ProductTable.tableInfo;

        public Either<UserError, Integer> create(String name) {
            return executeOne(createMergeExpr(merge ->
                merge.field(Field.name.value(name))
                    .where(Field.name)
            ));
        }

    }

    interface Field {
        DbColumn id = DbColumn.create("id");
        DbColumn name = DbColumn.create("name");
        DbColumn amount = DbColumn.create("amount");
        DbColumn updatedAt = DbColumn.create("updated_at");

        List<DbColumn> all = List.of(
            Field.id,
            Field.name,
            Field.amount,
            Field.updatedAt
        );
    }

    @Value
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    class Row {
        Integer id;
        String name;
        Integer amount;
        LocalDateTime updatedAt;
    }

}
