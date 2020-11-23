package jkit.db.model;

import jkit.core.ext.StringExt;
import jkit.core.model.FieldMap;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class DbColumn implements
    FieldMap.Key,
    IDbColumnConditionFactory,
    IDb.IDbColumn,
    DbColumnWithValue.Factory {

    @EqualsAndHashCode.Include
    String columnName;
    String jsonKeyName;
    Boolean isJson;

    public String getKey() {
        return jsonKeyName;
    }

    public DbColumn getColumn() {
        return this;
    }

    public static DbColumn create(
        String columnName,
        String jsonKeyName
    ) {
        return DbColumn.of(
            columnName,
            jsonKeyName,
            false
        );
    }

    public static DbColumn create(
        String columnName
    ) {
        return DbColumn.of(
            columnName,
            StringExt.snake2camel(columnName),
            false
        );
    }

    public static DbColumn createJson(
        String columnName
    ) {
        return DbColumn.of(
            columnName,
            StringExt.snake2camel(columnName),
            true
        );
    }

}


