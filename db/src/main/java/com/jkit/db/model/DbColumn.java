package com.jkit.db.model;

import com.jkit.core.ext.StringExt;
import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Value(staticConstructor = "of")
public class DbColumn implements
    IDbColumnConditionFactory,
    IDb.IDbColumn,
    DbColumnWithValue.Factory {

    @EqualsAndHashCode.Include
    String columnName;
    String jsonKeyName;
    Boolean isJson;

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


