package jkit.db.model;

import jkit.db.ISql;

public interface IDbColumnConditionFactory extends IDb.IDbColumnHolder {

    default ISql.WhereValue condition(
        Object value
    ) {
        return condition(
            "=",
            value
        );
    }

    default ISql.WhereValue condition(
        String operator,
        Object value
    ) {
        return ISql.WhereValue.createEquals(
            getColumn(),
            operator,
            value
        );
    }

    default ISql.WhereValue conditionIn(
        Object value
    ) {
        return ISql.WhereValue.createIn(
            getColumn(),
            value
        );
    }

}


