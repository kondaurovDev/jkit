package jkit.db;

import io.vavr.Function1;
import jkit.db.model.TableInfo;
import jkit.db.sql.MergeExpr;
import jkit.db.sql.SelectExpr;
import jkit.db.sql.UpdateExpr;

interface ICreateExpr {

    TableInfo<?> getTableInfo();

    default ISql.ReadyExpr createUpdateExpr(
        Function1<UpdateExpr.UpdateExprBuilder, UpdateExpr.UpdateExprBuilder> inner
    ) {
        return inner.apply(UpdateExpr.builder()).build().createExpr(getTableInfo().getTableName());
    }

    default ISql.ReadyExpr createMergeExpr(
        Function1<MergeExpr.MergeExprBuilder, MergeExpr.MergeExprBuilder> inner
    ) {
        return inner.apply(MergeExpr.builder()).build().createExpr(getTableInfo().getTableName());
    }

    default ISql.ReadyExpr createSelectExpr(
        Function1<SelectExpr.SelectExprBuilder, SelectExpr.SelectExprBuilder> inner
    ) {
        return inner.apply(SelectExpr.builder()).build().createExpr(getTableInfo().getTableName());
    }

}
