package com.jkit.db.model;

import lombok.*;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DbColumnWithValue
    implements
        IDb.IDbColumnWithValue {

    IDb.IDbColumn column;
    Object value;

    public IDb.IDbColumn getColumn() {
        return column;
    }

    public Object value() {
        return value;
    }

    public interface Factory extends IDb.IDbColumnHolder {

        default DbColumnWithValue value(Object value) {
            return new DbColumnWithValue(
                getColumn(),
                value
            );
        }

    }

}
