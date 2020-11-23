package jkit.db.model;

import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class TableInfo<M> {

    Class<M> modelClass;
    String tableName;
    Map<String, DbColumn> columnMap;

    public static <M> TableInfo<M> create(
        String tableName,
        List<DbColumn> columns,
        Class<M> clazz
    ) {
        return TableInfo.of(
            clazz,
            tableName,
            columns.toMap(f -> Tuple.of(f.getColumnName(), f))
        );
    }
}
